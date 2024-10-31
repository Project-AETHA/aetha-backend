package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.History;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<History, String> {
}
