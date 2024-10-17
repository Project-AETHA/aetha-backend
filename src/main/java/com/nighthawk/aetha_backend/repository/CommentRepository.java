package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
