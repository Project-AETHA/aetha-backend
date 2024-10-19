package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findAllByNovelAndStatusAndIsVisible(Novel novel, String status, Boolean isVisible);
    List<Chapter> findByNovel(Novel novel);
}
