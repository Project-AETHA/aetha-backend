package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BlogRepository extends MongoRepository<Blog, String> {
    Optional<Blog> findBlogByAuthor(String username);
}
