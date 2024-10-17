package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.AdDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AdRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ResponseDTO createAd(AdDTO adDTO, UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Ad newAd = new Ad();
        HashMap<String, String> errors = new HashMap<>();

        try {
            if(user == null) {
                errors.put("userDetails", "User not found");
                throw new Exception("User not found");
            }


            newAd.setCreator(user);
            newAd.setCreatedAt(new Date());
            newAd.setTitle(adDTO.getTitle());
            newAd.setContent(adDTO.getContent());
            newAd.setExpiresAt(adDTO.getExpiresAt());
            newAd.setIsActive(true);
            newAd.setBudget(adDTO.getBudget());
            newAd.setPricePlan(adDTO.getPricePlan());
            newAd.setCampaignType(adDTO.getCampaignType());
            newAd.setImageUrl(adDTO.getImageUrl());

            if (adDTO.getTitle() == null || adDTO.getTitle().isEmpty()) {
                errors.put("title", "Title is required");
            }

            if (adDTO.getContent() == null || adDTO.getContent().isEmpty()) {
                errors.put("content", "Content is required");
            }

            if (adDTO.getBudget() == null || adDTO.getBudget() <= 0) {
                errors.put("budget", "Budget must be greater than zero");
            }


            if(!errors.isEmpty()) {
                throw new Exception("Validation failed");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad created successfully");
            responseDTO.setContent(adRepository.save(newAd));

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            responseDTO.setErrors(errors);
        }

        return responseDTO;
    }

    public Ad findAdById(String id) {
        return adRepository.findById(id).orElse(null);
    }

    public List<AdDTO> findAllAds() {
        List<Ad> ads = adRepository.findAll();

        return ads.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .collect(Collectors.toList());
    }

    public ResponseDTO findMyAds(UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            responseDTO.setCode(VarList.RSP_TOKEN_INVALID);
            responseDTO.setMessage("User not found");
            responseDTO.setContent(null);
        } else {
            try {
                List<Ad> ads = adRepository.findByCreator(user);

                List<AdDTO> myAds = ads.stream()
                        .map(ad -> modelMapper.map(ad, AdDTO.class))
                        .collect(Collectors.toList());

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(myAds);

                if(myAds.isEmpty()) responseDTO.setMessage("No ads found");
                else responseDTO.setMessage("Ads found");

            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error fetching ads");
                responseDTO.setContent(null);
            }
        }

        return responseDTO;
    }

//    public List<AdDTO> searchAds(RequestDTO requestDTO) {
//        return adRepository.searchAds(
//                        requestDTO.getSearchTerm(),
//                        requestDTO.getCampaignType()
//                ).stream()
//                .map(ad -> modelMapper.map(ad, AdDTO.class))
//                .collect(Collectors.toList());
//    }

    @Transactional
    public ResponseDTO deleteAd(String id, UserDetails userDetails) {
        Ad ad = adRepository.findById(id).orElse(null);
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if (ad == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
        } else if (user == null) {
            responseDTO.setCode(VarList.RSP_TOKEN_INVALID);
            responseDTO.setMessage("User not found");
        } else if (ad.getCreator().getId().equals(user.getId()) || user.getRole().equals("ADMIN")) {
            if(ad.getImageUrl() != null) {
                File file = new File("public" + ad.getImageUrl());
                if(file.exists()) file.delete();
            }

            adRepository.delete(ad);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad deleted successfully");
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("You don't have permission to delete this ad");
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateAd(String id, AdDTO adDTO, UserDetails userDetails) {
        ResponseDTO responseDTO = new ResponseDTO();
        Ad existingAd = adRepository.findById(id).orElse(null);
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if (existingAd == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
            return responseDTO;
        }

        if (user == null) {
            responseDTO.setCode(VarList.RSP_TOKEN_INVALID);
            responseDTO.setMessage("User not found");
            return responseDTO;
        }

        // Check if the user is the creator of the ad
        if (!existingAd.getCreator().getId().equals(user.getId())) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("You don't have permission to update this ad");
            return responseDTO;
        }

        // Update fields if they are provided in the DTO
        if (adDTO.getTitle() != null) {
            existingAd.setTitle(adDTO.getTitle());
        }
        if (adDTO.getContent() != null) {
            existingAd.setContent(adDTO.getContent());
        }
        if (adDTO.getExpiresAt() != null) {
            existingAd.setExpiresAt(adDTO.getExpiresAt());
        }
        if (adDTO.getBudget() != null) {
            existingAd.setBudget(adDTO.getBudget());
        }
        if (adDTO.getPricePlan() != null) {
            existingAd.setPricePlan(adDTO.getPricePlan());
        }
        if (adDTO.getCampaignType() != null) {
            existingAd.setCampaignType(adDTO.getCampaignType());
        }
        if (adDTO.getImageUrl() != null) {
            existingAd.setImageUrl(adDTO.getImageUrl());
        }

        try {
            Ad updatedAd = adRepository.save(existingAd);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad updated successfully");
            responseDTO.setContent(modelMapper.map(updatedAd, AdDTO.class));
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error updating ad");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }


}