package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    
    List<Comment> findByNovelId(String novelId);
    List<Comment> findByUserId(String userId);

    @Query("{ 'content': { $regex: ?0, $options: 'i' } }")
    List<Comment> searchCommentsByContent(String content);

    @Query("{ 'novelId': ?0, 'createdAt': { $gte: ?1 } }")
    List<Comment> findByNovelIdAndCreatedAtAfter(String novelId, String createdAt);
}
