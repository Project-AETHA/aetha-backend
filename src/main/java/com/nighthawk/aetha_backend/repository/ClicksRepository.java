package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Click;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClicksRepository extends MongoRepository<Click, String> {
}
