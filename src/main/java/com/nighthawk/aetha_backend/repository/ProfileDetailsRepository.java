package com.nighthawk.aetha_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nighthawk.aetha_backend.entity.ProfileDetails;

public interface ProfileDetailsRepository extends MongoRepository<ProfileDetails, Long> {
    ProfileDetails findByUsername(String username);
}
