package com.datdev.controller;

import com.datdev.model.Image;
import com.datdev.model.Map;
import com.datdev.repo.MapRepo;
import com.datdev.utils.SearchUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ImageController {
    public static final String basePath = "./maps/";

    static final Pattern fileNamePattern = Pattern.compile("[\\w _\\-)`(\\[\\]*]{4,50}\\.(png|jpg)");

    final static Pattern searchLikeTagPattern = Pattern.compile("\\w{3,30}", Pattern.CASE_INSENSITIVE);
    final static Pattern searchNotLikeTagPattern = Pattern.compile("-(\\w{3,30})", Pattern.CASE_INSENSITIVE);
    final static Pattern searchExactlyTagPattern = Pattern.compile("\"(\\w{3,30})\"", Pattern.CASE_INSENSITIVE);
    final static Pattern searchGridTagPattern = Pattern.compile("([<>]?[\\d*]+?)x([<>]?[\\d*]+)", Pattern.CASE_INSENSITIVE);
    final static Pattern searchUserTagPattern = Pattern.compile("uploader:([\\w_-]{1,30})", Pattern.CASE_INSENSITIVE);


    @Autowired
    MapRepo mapRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "") String search,
                        @RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "50") Integer numPerPage) {
        List<Map> maps;

        String[] params = search.split("\\s+");
        StringBuilder sql = new StringBuilder();
        StringBuilder notSQL = new StringBuilder();

        boolean first = true;
        boolean firstNegative = true;

        Matcher match;
        for (String param : params) {
            String pattern = "";
            boolean negative = false;

            if ((match = searchGridTagPattern.matcher(param)).matches()) {
                pattern = "(squareWidth " + SearchUtils.parseSquare(match.group(1)) + " AND squareHeight " + SearchUtils.parseSquare(match.group(2)) + ")";
            } else if ((match = searchUserTagPattern.matcher(param)).matches()) {
                pattern = " uploader = '" + param + "'";
            } else if ((match = searchLikeTagPattern.matcher(param)).matches()) {
                pattern = " tag LIKE '%" + param + "%'";
            } else if ((match = searchNotLikeTagPattern.matcher(param)).matches()) {
                pattern = " tag NOT LIKE '%" + match.group(1) + "%'";
                negative = true;
            } else if ((match = searchExactlyTagPattern.matcher(param)).matches()) {
                pattern = " tag " + " = \"" + match.group(1) + "\"";
            }

            if (!pattern.isEmpty()) {
                if (negative) {
                    if (!firstNegative) {
                        notSQL.append(" OR");
                    } else {
                        firstNegative = false;
                    }

                    notSQL.append(pattern);
                } else {
                    if (!first) {
                        sql.append(" OR");
                    } else {
                        first = false;
                    }

                    sql.append(pattern);
                }
            }
        }

        String sqlString = "SELECT DISTINCT id, filePath, width, height, squareWidth, squareHeight, uploader, uploadDate " +
                "from maps left outer join tags on maps.id=tags.mapID ";

        String theSQL = sql.toString();
        String theNotSQL = notSQL.toString();

        if (!theSQL.isEmpty() || !theNotSQL.isEmpty()) {
            sqlString += "WHERE";
            if (!theSQL.isEmpty()) sqlString += "(" + sql.toString() + ") ";
            if (!theSQL.isEmpty() && !theNotSQL.isEmpty()) sqlString += "AND";
            if (!theNotSQL.isEmpty()) sqlString += " id NOT IN" +
                    "(SELECT DISTINCT id " +
                    "FROM maps left outer join tags on maps.id=tags.mapID " +
                    "WHERE" + notSQL.toString() + ")";
        }

        EntityManager em = emf.createEntityManager();

        maps = (List<Map>) em.createNativeQuery(sqlString).unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(Map.class)).getResultList();

        int theNumPerPage = Math.max(20, numPerPage);
        int pages = maps.size() / theNumPerPage;
        int thePage = Math.min(pages, page - 1);

        int start = thePage * theNumPerPage;
        int end = (thePage < pages ? (thePage + 1) * theNumPerPage : maps.size());

        model.addAttribute("maps", maps.subList(start, end));
        model.addAttribute("basePath", basePath);
        model.addAttribute("page", thePage);
        model.addAttribute("query", search);
        model.addAttribute("numPerPage", (theNumPerPage));
        return "/index";
    }

    @GetMapping("/image")
    public String image(Model model, @RequestParam(required = false) Integer id) {
        if (id == null) return "redirect:/";
        Optional<Map> map = mapRepository.findById(id);
        if (map.isEmpty()) return "redirect:/";

        model.addAttribute("map", map.get());
        model.addAttribute("basePath", basePath);
        return "/image";
    }

    @GetMapping(value = "/getTags", produces = "application/json")
    @ResponseBody
    public List<String> getTags(){
        return mapRepository.findDistinctTags();
    }

    @PostMapping("/imageTags")
    @ResponseBody
    public ResponseEntity<String> imageTitle(@RequestParam int id, @RequestBody String[] newTags) {
        Optional<Map> map = mapRepository.findById(id);

        if (map.isEmpty()) {
            return new ResponseEntity<>("Unknown Map ID", HttpStatus.BAD_REQUEST);
        } else {
            map.get().setTags(new ArrayList<>());
            Arrays.stream(newTags).forEach(tag -> map.get().addTag(tag));
            mapRepository.save(map.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    Path getUnownedPath(Path basePath, String name, String extension) throws InvalidFileNameException {
        Path thePath = basePath.resolve(name + "." + extension);
        if (!Files.exists(thePath)) {
            return thePath;
        }

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            thePath = basePath.resolve(name + i + "." + extension);
            if (!Files.exists(thePath)) return thePath;
        }

        throw new InvalidFileNameException("FFS", "FFS");
    }

    @PostMapping(value = "/imageUpload", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<String> imageUpload(@ModelAttribute Image image) throws IOException, NoSuchAlgorithmException {
        System.out.println("Received image from " + image.name);

        // Check Hash
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] data = image.image.getInputStream().readAllBytes();
        String imageHash = DigestUtils.md5DigestAsHex(data);

        if (mapRepository.existsByImageHash(imageHash)) {
            System.out.println("Image already uploaded, discarding");
            return new ResponseEntity<>("00 - Image already uploaded", HttpStatus.BAD_REQUEST);
        }


        String name = image.image.getOriginalFilename();

        Matcher fileName = fileNamePattern.matcher("name");

        if(!fileName.matches()) {
            return new ResponseEntity<>("01 - Bad name format, please clean it up", HttpStatus.BAD_REQUEST);
        }

        String directory = "uploaded/";
        BufferedImage bufferedImage = ImageIO.read(image.image.getInputStream());

        Path path = Paths.get(basePath + directory);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String[] theName = name.split("\\.");
        try (InputStream is = image.image.getInputStream()) {

            Path filePath = getUnownedPath(path, theName[0], theName[1]);
            Files.copy(is, filePath);
        } catch (InvalidFileNameException e) {
            System.out.println("There is Somehow 2,147,483,647 files with the name" + name + ", this request was rejected");
            return new ResponseEntity<>("02 - Somehow there are already 2,147,483,648 files with that name, try a different one", HttpStatus.BAD_REQUEST);
        }

        Map map = new Map(directory + name, bufferedImage.getWidth(), bufferedImage.getHeight(), image.squareWidth, image.squareHeight, image.name, imageHash);

        for (String tag : image.tags) {
            map.addTag(tag);
        }

        map.addTag("uploaded");
        map.addTag(image.name);

        mapRepository.save(map);

        System.out.println("Added Map: " + map.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
