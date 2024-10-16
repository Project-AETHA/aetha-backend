package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nighthawk.aetha_backend.utils.StatusList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface NovelRepository extends MongoRepository<Novel, String> {
    Page<Novel> findByAuthor(AuthUser author, Pageable pageable);
    Page<Novel> findByTitle(String title, Pageable pageable);
    long countByStatus(StatusList status);
    long countByPublishedAtBetween(LocalDate startDate, LocalDate endDate);
}
