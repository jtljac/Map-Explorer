package com.datdev.controller;

import com.datdev.enums.SearchDir;
import com.datdev.enums.SearchMode;
import com.datdev.model.Map;
import com.datdev.model.MapCollection;
import com.datdev.model.MapCollectionReduced;
import com.datdev.model.MapReduced;
import com.datdev.repo.CollectionRepo;
import com.datdev.repo.MapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CollectionApiController {
    @Autowired
    CollectionRepo collectionRepository;

    @Autowired
    MapRepo mapRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @GetMapping(value = "/api/collection")
    @ResponseBody
    public ResponseEntity<List<MapCollectionReduced>> getCollections(@RequestParam(required = false, defaultValue = "name") String order,
                                                                     @RequestParam(required = false, defaultValue = "asc") String orderdir,
                                                                     @RequestParam(required = false, defaultValue = "0") int offset,
                                                                     @RequestParam(required = false, defaultValue = "30") int numPerPage) {
        List<MapCollection> collections = collectionRepository.findAll(PageRequest.of(offset, numPerPage)).getContent();

        ResponseEntity<List<MapCollectionReduced>> entity;
        if (collections.isEmpty()) entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else entity = new ResponseEntity<>(collections.stream().map(MapCollectionReduced::new).collect(Collectors.toList()), HttpStatus.OK);

        return entity;
    }

    @PostMapping(value = "/api/collection", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> addNewCollection(@RequestBody MapCollectionReduced collection) {
        if (collection.getName() == null || collection.getName().isEmpty()) return new ResponseEntity<>("01 - The collection needs a name", HttpStatus.BAD_REQUEST);
        if (collection.getCreator() == null || collection.getCreator().isEmpty()) return new ResponseEntity<>("02 - The collection needs a creator", HttpStatus.BAD_REQUEST);
        if (collectionRepository.existsByName(collection.getName())) return new ResponseEntity<>("03 - A collection by that name already exists", HttpStatus.BAD_REQUEST);

        MapCollection theCollection = collectionRepository.save(new MapCollection(collection.getName(), collection.getCreator(), collection.getDescription()));

        return new ResponseEntity<>(String.valueOf(theCollection.getId()), HttpStatus.OK);
    }

    @GetMapping(value = "/api/collection/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<MapCollectionReduced> getCollection(@PathVariable int id) {
        Optional<MapCollection> collection = collectionRepository.findById(id);
        return collection.map(value -> new ResponseEntity<>(new MapCollectionReduced(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/api/collection/{id}/")
    @ResponseBody
    public ResponseEntity<String> removeCollection(@PathVariable int id) {
        Optional<MapCollection> collection = collectionRepository.findById(id);

        if (collection.isEmpty()) return new ResponseEntity<>("01 - Unknown Collection", HttpStatus.BAD_REQUEST);

        collectionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/api/collection/{id}/maps/{mapid}")
    @ResponseBody
    public ResponseEntity<String> addMapToCollection(@PathVariable int id, @PathVariable int mapid) {
        Optional<MapCollection> collection = collectionRepository.findById(id);
        if (collection.isEmpty()) return new ResponseEntity<>("01 - Unknown collection", HttpStatus.BAD_REQUEST);

        Optional<Map> map = mapRepository.findById(mapid);
        if (map.isEmpty()) return new ResponseEntity<>("01 - Unknown map", HttpStatus.BAD_REQUEST);
        collection.get().getMaps().add(map.get());

        MapCollection theCollection = collectionRepository.save(collection.get());
        return new ResponseEntity<>(String.valueOf(theCollection.getId()), HttpStatus.OK);
    }

    @PostMapping(value = "/api/collection/{id}/maps")
    @ResponseBody
    public ResponseEntity<?> addMapsToCollection(@PathVariable int id, @RequestBody List<Integer> maps) {
        Optional<MapCollection> collection = collectionRepository.findById(id);
        if (collection.isEmpty()) return new ResponseEntity<>("01 - Unknown collection", HttpStatus.BAD_REQUEST);

        for (int mapid : maps) {
            Optional<Map> map = mapRepository.findById(mapid);
            map.ifPresent(value -> collection.get().getMaps().add(value));
        }

        MapCollection theCollection = collectionRepository.save(collection.get());
        return new ResponseEntity<>(String.valueOf(theCollection.getId()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/collection/{id}/maps/{mapid}")
    @ResponseBody
    public ResponseEntity<?> removeMapsFromCollection(@PathVariable int id, @PathVariable int mapid) {
        Optional<MapCollection> collection = collectionRepository.findById(id);
        if (collection.isEmpty()) return new ResponseEntity<>("01 - Unknown collection", HttpStatus.BAD_REQUEST);

        List<Map> maps = collection.get().getMaps();
        for (int i = 0; i < maps.size(); ++i) {
            if (maps.get(i).getId() == mapid) {
                maps.remove(i);

                collectionRepository.save(collection.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("02 - Map not present in collection", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/api/collection/{id}/maps")
    @ResponseBody
    public ResponseEntity removeMapsFromCollection(@PathVariable int id, @RequestBody List<Integer> maps) {
        Optional<MapCollection> collection = collectionRepository.findById(id);
        if (collection.isEmpty()) return new ResponseEntity<>("01 - Unknown collection", HttpStatus.BAD_REQUEST);

        collection.get().getMaps().clear();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/api/collection/{id}/maps", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<MapReduced>> getCollectionMaps(@PathVariable int id,
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

        String sqlString =  "SELECT DISTINCT id, filePath, thumbnail, width, height, squareWidth, squareHeight, author, uploader, uploadDate, imageHash, toReview " +
                            "FROM maps INNER JOIN mapCollection ON maps.id=mapCollection.mapID " +
                            "WHERE collectionID =" + id;
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
}
