package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.ProfileReview;
import com.nighthawk.aetha_backend.repository.ProfileReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProfileReviewService {
    private final ProfileReviewRepository profileReviewRepository;

    public ProfileReviewService(ProfileReviewRepository profileReviewRepository) {
        this.profileReviewRepository = profileReviewRepository;
    }

    public List<ProfileReview> getAllReviews() {
        return profileReviewRepository.findAll();
    }

    public ProfileReview saveReview(ProfileReview profileReview) {
        return profileReviewRepository.save(profileReview);
    }
}
