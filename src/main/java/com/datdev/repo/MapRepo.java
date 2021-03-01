package com.datdev.repo;

import com.datdev.model.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MapRepo extends CrudRepository<Map, Integer> {
    List<Map> findDistinctByTagsIn(Set<String> tags);

    @Query(value = "SELECT DISTINCT tag FROM tags ORDER BY tag ASC", nativeQuery = true)
    List<String> findDistinctTags();

    @Query(value = "SELECT id FROM maps WHERE id > ?1 ORDER BY id LIMIT 1", nativeQuery = true)
    Optional<Integer> findNextID(Integer currentID);

    @Query(value = "SELECT id FROM maps WHERE id < ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Integer> findPrevID(Integer currentID);

    boolean existsByImageHash(String hash);
}
