package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Click;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClicksRepository extends MongoRepository<Click, String> {
    int countClicksByNovel(Novel novel);
}
