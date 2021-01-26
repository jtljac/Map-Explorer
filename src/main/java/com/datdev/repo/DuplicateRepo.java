package com.datdev.repo;

import com.datdev.model.Duplicates;
import com.datdev.model.DuplicatesKey;
import org.springframework.data.repository.CrudRepository;

public interface DuplicateRepo extends CrudRepository<Duplicates, DuplicatesKey> {
}
