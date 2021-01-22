package com.datdev.controller;

import com.datdev.MapExplorerApplication;
import com.datdev.model.Image;
import com.datdev.model.Map;
import com.datdev.model.MapReduced;
import com.datdev.repo.MapRepo;
import com.datdev.utils.FileUtil;
import com.datdev.utils.SearchUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class apiController {
    static final Pattern fileNamePattern = Pattern.compile("[\\w _\\-)`(\\[\\]*]{4,50}\\.(png|jpg)");
    static final Pattern userNamePattern = Pattern.compile("[\\w_-]{1,25}");

    @Autowired
    MapRepo mapRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @GetMapping(value = "/getTags", produces = "application/json")
    @ResponseBody
    public List<String> getTags(){
        return mapRepository.findDistinctTags();
    }

    @GetMapping(value = "/getImage", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map> getImage(@RequestParam int id) {
        Optional<Map> map = mapRepository.findById(id);
        return map.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/getImages", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<MapReduced>> getImages(@RequestParam(required = false, defaultValue = "") String search,
                                                      @RequestParam(required = false, defaultValue = "0") int offset,
                                                      @RequestParam(required = false, defaultValue = "30") int numPerPage) {
        EntityManager em = emf.createEntityManager();

        int theNumPerPage = Math.max(numPerPage, 0);
        int theOffset = Math.max(offset, 0);

        String sqlString = SearchUtils.createSearchStatement(search);
        sqlString += " ORDER BY filePath ASC";
        sqlString += " LIMIT " + theNumPerPage + " OFFSET " + theOffset;

        System.out.println("Getting Maps");
        List<Map> maps = em.createNativeQuery(sqlString, Map.class).getResultList();


        ResponseEntity<List<MapReduced>> entity;
        if (maps.isEmpty()) entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else entity = new ResponseEntity<>(maps.stream().map(MapReduced::new).collect(Collectors.toList()), HttpStatus.OK);

        em.close();
        return entity;
    }

    @PostMapping("/setTags")
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

    @PostMapping(value = "/uploadImage", consumes = {"multipart/form-data"})
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

        if(!fileNamePattern.matcher(name).matches()) {
            System.out.println(name + " is a really bad name, discarding");
            return new ResponseEntity<>("01 - Bad name format, please clean it up", HttpStatus.BAD_REQUEST);
        }

        if (!userNamePattern.matcher(image.name).matches()) {
            System.out.println(image.name + " is a really bad username, discarding");
            return new ResponseEntity<>("02 - Your username is bad and you should feel bad", HttpStatus.BAD_REQUEST);
        }

        String directory = "uploaded/" + image.name + "/";

        Path path = Paths.get(MapExplorerApplication.basePath + directory);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String[] theName = name.split("\\.");
        try (InputStream is = image.image.getInputStream()) {

            Path filePath = FileUtil.getUnownedPath(path, theName[0], theName[1]);
            Files.copy(is, filePath);
        } catch (InvalidFileNameException e) {
            System.out.println("There is Somehow 2,147,483,647 files with the name" + name + ", this request was rejected");
            return new ResponseEntity<>("03 - Somehow there are already 2,147,483,648 files with that name, try a different one", HttpStatus.BAD_REQUEST);
        }

        BufferedImage bufferedImage = ImageIO.read(image.image.getInputStream());

        Map map = new Map(directory + name, bufferedImage.getWidth(), bufferedImage.getHeight(), image.squareWidth, image.squareHeight, image.name, imageHash);

        for (String tag : image.tags) {
            map.addTag(tag);
        }

        map.addTag("uploaded");

        mapRepository.save(map);

        System.out.println("Added Map: " + map.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
