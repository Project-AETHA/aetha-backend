package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByNovel(Novel novel);
    List<Comment> findByUser(AuthUser user);
}



