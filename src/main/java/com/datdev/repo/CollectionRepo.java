package com.datdev.repo;

import com.datdev.model.MapCollection;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CollectionRepo extends PagingAndSortingRepository<MapCollection, Integer> {
    boolean existsByName(String name);
}
