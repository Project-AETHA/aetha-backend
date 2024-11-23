package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.ProfileDetails;
import com.nighthawk.aetha_backend.repository.ProfileDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileDetailsService {

    @Autowired
    private ProfileDetailsRepository profileDetailsRepository;

    public ProfileDetails getProfileByUsername(String username) {
        return profileDetailsRepository.findByUsername(username);
    }

    public ProfileDetails saveProfileDetails(ProfileDetails profileDetails) {
        return profileDetailsRepository.save(profileDetails);
    }

    public ProfileDetails updateProfileDetails(Long id, ProfileDetails updatedDetails) {
        Optional<ProfileDetails> existingProfile = profileDetailsRepository.findById(id);
        if (existingProfile.isPresent()) {
            ProfileDetails profile = existingProfile.get();
            profile.setBio(updatedDetails.getBio());
            profile.setGender(updatedDetails.getGender());
            profile.setLocation(updatedDetails.getLocation());
            profile.setWeb(updatedDetails.getWeb());
            profile.setTwitter(updatedDetails.getTwitter());
            profile.setFacebook(updatedDetails.getFacebook());
            // Set other updatable fields
            return profileDetailsRepository.save(profile);
        }
        throw new RuntimeException("Profile not found with ID: " + id);
    }
}
