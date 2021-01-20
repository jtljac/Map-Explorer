package com.datdev.repo;

import com.datdev.model.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface MapRepo extends CrudRepository<Map, Integer> {
    List<Map> findDistinctByTagsIn(Set<String> tags);

    @Query(value = "SELECT DISTINCT tag FROM Tags ORDER BY tag ASC", nativeQuery = true)
    List<String> findDistinctTags();

    boolean existsByImageHash(String hash);
}
