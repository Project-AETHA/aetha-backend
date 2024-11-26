package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.entity.ProfileReview;
import com.nighthawk.aetha_backend.service.ProfileReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-reviews")

public class ProfileReviewController {
    private final ProfileReviewService profileReviewService;

    public ProfileReviewController(ProfileReviewService profileReviewService) {
        this.profileReviewService = profileReviewService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileReview>> getAllReviews() {
        List<ProfileReview> reviews = profileReviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ProfileReview> addReview(@RequestBody ProfileReview profileReview) {
        ProfileReview savedReview = profileReviewService.saveReview(profileReview);
        return ResponseEntity.ok(savedReview);
    }
}
