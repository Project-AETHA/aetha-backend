package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Forum;
import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ForumRepository extends MongoRepository<Forum, String> {

    List<Forum> findByUser(AuthUser user);
    List<Forum> findByTopic(String topic);
    
    
}
