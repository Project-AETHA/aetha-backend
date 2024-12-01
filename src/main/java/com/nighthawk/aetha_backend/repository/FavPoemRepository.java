package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.FavPoem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavPoemRepository extends MongoRepository<FavPoem, String> {
}
