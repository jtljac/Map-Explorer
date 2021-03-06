package com.datdev.controller;

import com.datdev.MapExplorerApplication;
import com.datdev.enums.SearchDir;
import com.datdev.enums.SearchMode;
import com.datdev.model.Image;
import com.datdev.model.Map;
import com.datdev.model.MapReduced;
import com.datdev.model.MapUpdate;
import com.datdev.repo.MapRepo;
import com.datdev.utils.FileUtil;
import com.datdev.utils.SearchUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class apiController {
    static final Pattern fileNamePattern = Pattern.compile("[\\w \\-)`(\\[\\]*]{4,50}\\.(png|jpg)");
    static final Pattern userNamePattern = Pattern.compile("[\\w-]{1,25}");

    @Autowired
    MapRepo mapRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @GetMapping(value = "/api/tags", produces = "application/json")
    @ResponseBody
    public List<String> getTags(){
        return mapRepository.findDistinctTags();
    }

    @GetMapping(value = "/api/images/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map> getImage(@PathVariable int id) {
        Optional<Map> map = mapRepository.findById(id);
        return map.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/api/images", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<MapReduced>> getImages(@RequestParam(required = false, defaultValue = "") String search,
                                                      @RequestParam(required = false, defaultValue = "name") String order,
                                                      @RequestParam(required = false, defaultValue = "asc") String orderdir,
                                                      @RequestParam(required = false, defaultValue = "0") int offset,
                                                      @RequestParam(required = false, defaultValue = "30") int numPerPage) {
        EntityManager em = emf.createEntityManager();

        int theNumPerPage = Math.max(numPerPage, 0);
        int theOffset = Math.max(offset, 0);

        SearchMode searchMode;
        try {
            searchMode = SearchMode.valueOf(order.toUpperCase());
        } catch (IllegalArgumentException error) {
            searchMode = SearchMode.NAME;
        }

        SearchDir searchDir;
        try {
            searchDir = SearchDir.valueOf(orderdir.toUpperCase());
        } catch (IllegalArgumentException error) {
            searchDir = SearchDir.ASC;
        }

        String sqlString = SearchUtils.createSearchStatement(search);
        sqlString += " ORDER BY ";
        switch (searchMode) {
            case NAME:
                sqlString += "filePath";
                break;
            case UPLOADER:
                sqlString += "uploader";
                break;
            case DATE:
                sqlString += "uploadDate";
                break;
            case RESOLUTION:
                sqlString += "width*height";
                break;
            case GRIDSIZE:
                sqlString += "squareWidth*SquareHeight";
                break;
            case RANDOM:
                sqlString += "RAND()";
                break;
        }
        sqlString += " " + searchDir.toString();

        sqlString += " LIMIT " + theNumPerPage + " OFFSET " + theOffset;

        System.out.println("Getting Maps");
        List<Map> maps = em.createNativeQuery(sqlString, Map.class).getResultList();


        ResponseEntity<List<MapReduced>> entity;
        if (maps.isEmpty()) entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else entity = new ResponseEntity<>(maps.stream().map(MapReduced::new).collect(Collectors.toList()), HttpStatus.OK);

        em.close();
        return entity;
    }

    @DeleteMapping("/api/images/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable int id) {
        Optional<Map> map = mapRepository.findById(id);

        if (map.isPresent()) {
            try {
                Files.deleteIfExists(Path.of(MapExplorerApplication.basePath + map.get().getFilePath()));
                if (map.get().getThumbnail()) {
                    Files.deleteIfExists(Path.of(MapExplorerApplication.basePath + map.get().getThumbnailPath()));
                }
            } catch (IOException e) {
                System.out.println("Failed to delete file" + map.get().getFilePath());
            }

            mapRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("00 - Unknown Image", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/api/images/{id}/tags")
    @ResponseBody
    public ResponseEntity<String> setImageTags(@PathVariable int id, @RequestBody String[] newTags) {
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

    @PutMapping(value = "/api/images/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<String> updateImageInfo(@PathVariable int id, @RequestBody MapUpdate theMap) {
        Optional<Map> map = mapRepository.findById(id);

        if (map.isEmpty()) {
            return new ResponseEntity<>("0 - Unknown Map ID", HttpStatus.BAD_REQUEST);
        }

        if (theMap.getUploader() == null || !userNamePattern.matcher(theMap.getUploader()).matches()) {
            return new ResponseEntity<>("1 - Bad Username, it should only contain letters, numbers, dashes, and underscores", HttpStatus.BAD_REQUEST);
        }

        if (theMap.getAuthor() != null && !userNamePattern.matcher(theMap.getAuthor()).matches()) {
            return new ResponseEntity<>("2 - Bad Author name, it should only contain letters, numbers, dashes, and underscores", HttpStatus.BAD_REQUEST);
        }

        map.get().updateValues(theMap);
        mapRepository.save(map.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/api/images", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<String> uploadImage(@ModelAttribute Image image) throws IOException, NoSuchAlgorithmException {
        System.out.println("Received image from " + image.name);

        // Check Hash
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream stream = image.image.getInputStream();
        byte[] data = new byte[2048];
        int numBytes;
        while ((numBytes = stream.read(data)) != -1) {
            md.update(data, 0, numBytes);
        }
        stream.close();

        String imageHash = DatatypeConverter.printHexBinary(md.digest()).toLowerCase();

        System.out.println("Calculated image hash: " + imageHash);

        if (mapRepository.existsByImageHash(imageHash)) {
            System.out.println("Image already uploaded, discarding");
            return new ResponseEntity<>("00 - Image already uploaded", HttpStatus.BAD_REQUEST);
        }

        String name = image.image.getOriginalFilename();

        if(!fileNamePattern.matcher(name).matches()) {
            System.out.println(name + " is a really bad name, discarding");
            return new ResponseEntity<>("01 - Bad name format, please clean it up", HttpStatus.BAD_REQUEST);
        }

        if (image.name == null || !userNamePattern.matcher(image.name).matches()) {
            System.out.println(image.name + " is a really bad username, discarding");
            return new ResponseEntity<>("02 - Your username is bad and you should feel bad, it should only contain letters, numbers, dashes, and underscores", HttpStatus.BAD_REQUEST);
        }

        if (image.author != null && !userNamePattern.matcher(image.author).matches()) {
            System.out.println(image.author + " is a really bad author name, discarding");
            return new ResponseEntity<>("03 - That author name is bad and the author should feel bad, it should only contain letters, numbers, dashes, and underscores", HttpStatus.BAD_REQUEST);
        }

        String directory = "uploaded/" + image.name + "/";

        Path path = Paths.get(MapExplorerApplication.basePath + directory);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String fileName;
        String[] theName = name.split("\\.");
        try {
            System.out.println("Saving to disk");
            fileName = FileUtil.getUnownedFileName(path, theName[0], theName[1]);
            stream = image.image.getInputStream();
            Files.copy(stream, path.resolve(fileName));
            stream.close();
        } catch (InvalidFileNameException e) {
            System.out.println("There is Somehow 2,147,483,647 files with the name" + name + ", this request was rejected");
            return new ResponseEntity<>("03 - Somehow there are already 2,147,483,648 files with that name, try a different one", HttpStatus.BAD_REQUEST);
        }

        System.out.println("Getting Resolution");

        stream = image.image.getInputStream();
        int width = 0, height = 0;

        try (ImageInputStream in = ImageIO.createImageInputStream(stream)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    width = reader.getWidth(0);
                    height = reader.getHeight(0);
                } finally {
                    reader.dispose();
                }
            }
        }

        stream.close();

        Map map = new Map(directory + fileName, width, height, image.squareWidth, image.squareHeight, image.name, imageHash);

        for (String tag : image.tags) {
            map.addTag(tag);
        }

        map.addTag("uploaded");

        map = mapRepository.save(map);

        System.out.println("Added Map: " + map.toString());

        return new ResponseEntity<>(String.valueOf(map.getId()), HttpStatus.OK);
    }
}
