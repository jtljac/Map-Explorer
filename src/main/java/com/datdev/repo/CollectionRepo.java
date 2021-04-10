package com.datdev.repo;

import com.datdev.model.MapCollection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface CollectionRepo extends CrudRepository<MapCollection, Integer> {
    boolean existsByName(String name);

    List<MapCollection> findAll(Pageable pageable);
}
