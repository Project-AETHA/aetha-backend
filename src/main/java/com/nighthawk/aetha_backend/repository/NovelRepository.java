package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NovelRepository extends MongoRepository<Novel, String> {
    Optional<Novel> findNovelByTitle(String title);
    Optional<Novel> findNovelByAuthor(AuthUser author);
}
