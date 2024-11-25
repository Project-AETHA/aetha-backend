package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.ShortStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nighthawk.aetha_backend.utils.StatusList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShortStoryRepository extends MongoRepository<ShortStory, String> {
    List<ShortStory> findByAuthor(AuthUser author);
    Page<ShortStory> findByAuthor(AuthUser author, Pageable pageable);
    Page<ShortStory> findByTitle(String title, Pageable pageable);
    long countByStatus(StatusList status);
    long countByPublishedAtBetween(LocalDate startDate, LocalDate endDate);
}
