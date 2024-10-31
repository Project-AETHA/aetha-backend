package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findAllByNovelAndStatusAndIsVisible(Novel novel, String status, Boolean isVisible);
    List<Chapter> findByNovel(Novel novel);
    Optional<Chapter> findByNovelIdAndChapterNumber(String novelId, Integer chapterNumber);
    int countChaptersByNovelIdAndIsVisibleAndStatus(String novel_id, Boolean isVisible, String status);
}
