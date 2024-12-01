package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByChapter(Chapter chapter);
    List<Comment> findByUser(AuthUser user);
    List<Comment> findByChapterAndUser(Chapter chapter, AuthUser user);
}




