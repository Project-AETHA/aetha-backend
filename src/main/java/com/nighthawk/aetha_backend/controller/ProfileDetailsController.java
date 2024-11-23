package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.entity.ProfileDetails;
import com.nighthawk.aetha_backend.service.ProfileDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileDetailsController {

    @Autowired
    private ProfileDetailsService profileDetailsService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDetails> getProfile(@PathVariable String username) {
        ProfileDetails profile = profileDetailsService.getProfileByUsername(username);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<ProfileDetails> createProfile(@RequestBody ProfileDetails profileDetails) {
        ProfileDetails savedProfile = profileDetailsService.saveProfileDetails(profileDetails);
        return ResponseEntity.ok(savedProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDetails> updateProfile(
            @PathVariable Long id,
            @RequestBody ProfileDetails updatedDetails
    ) {
        ProfileDetails updatedProfile = profileDetailsService.updateProfileDetails(id, updatedDetails);
        return ResponseEntity.ok(updatedProfile);
    }
}

