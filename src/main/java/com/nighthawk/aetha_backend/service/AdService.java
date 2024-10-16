package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AdRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
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

    @Autowired
    private ResponseDTO responseDTO;

    private AuthUser validateUser(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
    }

    public ResponseDTO createAd(Ad ad, UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user != null) {
            try {
                ad.setCreator(user);
                ad.setCreatedAt(new Date());
                ad.setIsActive(true);
                Ad savedAd = adRepository.save(ad);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad created successfully");
                responseDTO.setContent(savedAd);
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error while creating ad: " + e.getMessage());
                responseDTO.setContent(null);
            }
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found or not authenticated");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public ResponseDTO findAdById(String id, UserDetails userDetails) {

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if (user == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found or not authenticated");
            responseDTO.setContent(null);
            return responseDTO;
        }

        Ad ad = adRepository.findById(id).orElse(null);
        if(ad != null) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad found");
            responseDTO.setContent(ad);
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Ad not found");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public ResponseDTO findAllAds(UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found or not authenticated");
            responseDTO.setContent(null);
            return responseDTO;
        }

        List<Ad> ads = adRepository.findAll();
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("All ads retrieved");
        responseDTO.setContent(ads);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteAd(String id, UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if (user == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found or not authenticated");
            responseDTO.setContent(null);
            return responseDTO;
        }

        Ad ad = adRepository.findById(id).orElse(null);
        if (ad == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Ad not found");
            responseDTO.setContent(null);
            return responseDTO;
        }

        if(ad.getCreator().getId().equals(user.getId()) || user.getRole().equals("ADMIN")) {
            try {
                adRepository.delete(ad);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad deleted successfully");
                responseDTO.setContent(ad);
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error while deleting ad: " + e.getMessage());
                responseDTO.setContent(null);
            }
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Not authorized to delete this ad");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateAd(String id, Ad ad, UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found or not authenticated");
            responseDTO.setContent(null);
            return responseDTO;
        }

        Ad existingAd = adRepository.findById(id).orElse(null);
        if (existingAd == null) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Ad not found");
            responseDTO.setContent(null);
            return responseDTO;
        }

        if(existingAd.getCreator().getId().equals(user.getId())) {
            try {
                updateAdFields(existingAd, ad);
                Ad updatedAd = adRepository.save(existingAd);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad updated successfully");
                responseDTO.setContent(updatedAd);
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error while updating ad: " + e.getMessage());
                responseDTO.setContent(null);
            }
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Not authorized to update this ad");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    private void updateAdFields(Ad existingAd, Ad newAd) {
        if (newAd.getTitle() != null) existingAd.setTitle(newAd.getTitle());
        if (newAd.getContent() != null) existingAd.setContent(newAd.getContent());
        if (newAd.getImageUrl() != null) existingAd.setImageUrl(newAd.getImageUrl());
        if (newAd.getExpiresAt() != null) existingAd.setExpiresAt(newAd.getExpiresAt());
        if (newAd.getIsActive() != null) existingAd.setIsActive(newAd.getIsActive());
    }
}