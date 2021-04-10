package com.datdev.repo;

import com.datdev.model.MapCollection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CollectionRepo extends CrudRepository<MapCollection, Integer> {
    boolean existsByName(String name);

    @Query(value = "SELECT m FROM MapCollection m")
    List<MapCollection> findAll(Pageable pageable);
}
