package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NovelRepository extends MongoRepository<Novel, String> {
    Optional<Novel> findNovelByTitle(String title);
    Page<Novel> findByAuthor(AuthUser author, Pageable pageable);
    Page<Novel> findByTitle(String title, Pageable pageable);
}
