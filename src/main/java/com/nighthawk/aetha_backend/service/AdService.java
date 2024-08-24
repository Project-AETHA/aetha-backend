package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AdRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AuthUserRepository userRepository;

    public Ad createAd(Ad ad, UserDetails userDetails) {

        // Get the user details of the person currently logged in to create the ad
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return null;
        }

        // Set the creator of the ad to the user who is creating it
        ad.setCreator(user);
        ad.setCreatedAt(new Date());
        ad.setIsActive(true);  // Mark the ad as active when created

        // TODO: Validation rules for the ad content

        return adRepository.save(ad);
    }

    public Ad findAdById(String id) {
        return adRepository.findById(id).orElse(null);
    }

    public List<Ad> findAllAds() {
        return adRepository.findAll();
    }

    @Transactional
    public Boolean deleteAd(Ad ad, UserDetails userDetails) {
        if (ad == null) {
            return false;
        }

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return false;
        }

        if(ad.getCreator().getId().equals(user.getId()) || user.getRole().equals("ADMIN")) {
            adRepository.delete(ad);
            return true;
        }

        return false;
    }

    @Transactional
    public Ad updateAd(String id, Ad ad, UserDetails userDetails) {
        Ad existingAd = adRepository.findById(id).orElse(null);

        if (existingAd == null) {
            return null;
        }

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return null;
        }

        // Only allow the creator of the ad to update it
        if(existingAd.getCreator().getId().equals(user.getId())) {
            // TODO: Validate the ad details before updating

            if (existingAd.getId() != null && ad.getId() == null) {
                ad.setId(existingAd.getId());
            }
            if (existingAd.getTitle() != null && ad.getTitle() == null) {
                ad.setTitle(existingAd.getTitle());
            }
            if (existingAd.getContent() != null && ad.getContent() == null) {
                ad.setContent(existingAd.getContent());
            }
            if (existingAd.getImageUrl() != null && ad.getImageUrl() == null) {
                ad.setImageUrl(existingAd.getImageUrl());
            }
            if (existingAd.getCreatedAt() != null) {
                ad.setCreatedAt(existingAd.getCreatedAt());
            }
            if (existingAd.getExpiresAt() != null && ad.getExpiresAt() == null) {
                ad.setExpiresAt(existingAd.getExpiresAt());
            }
            if (existingAd.getIsActive() != null) {
                ad.setIsActive(existingAd.getIsActive());
            }

            return adRepository.save(ad);
        }

        return null;
    }
}
