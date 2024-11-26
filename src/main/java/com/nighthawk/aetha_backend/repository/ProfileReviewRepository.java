package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.ProfileReview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileReviewRepository extends MongoRepository<ProfileReview, Long> {
}
