package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends MongoRepository<Ad, String> {
    List<Ad> findByCreator(AuthUser creator);
    Ad findByTitle(String title);
}