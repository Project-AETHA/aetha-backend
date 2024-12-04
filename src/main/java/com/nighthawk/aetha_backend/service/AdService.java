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
    public ResponseDTO createPendingAd(AdDTO adDTO, UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Ad newAd = new Ad();
        Ad savedAd = new Ad();

        HashMap<String, String> errors = new HashMap<>();

        try {
            if(user == null) {
                errors.put("userDetails", "User not found");
                throw new Exception("User not found");
            }

            // Map fields from DTO to entity
            newAd.setCreator(user);
            newAd.setCreatedAt(new Date());
            newAd.setInternalTitle(adDTO.getInternalTitle());
            newAd.setAdType(adDTO.getAdType());
            newAd.setBackgroundImage(adDTO.getBackgroundImage());
            newAd.setRedirectLink(adDTO.getRedirectLink());
            newAd.setStartDate(adDTO.getStartDate());
            newAd.setEndDate(adDTO.getEndDate());
            newAd.setCalculatedPrice(adDTO.getCalculatedPrice());
            newAd.setIsActive(true);
            newAd.setStatus("PAYMENT_PENDING");

            // Validation
            if (adDTO.getInternalTitle() == null || adDTO.getInternalTitle().isEmpty()) {
                errors.put("internalTitle", "Internal title is required");
            }

            if (adDTO.getAdType() == null || adDTO.getAdType().isEmpty()) {
                errors.put("adType", "Ad type is required");
            }

            if (adDTO.getRedirectLink() == null || adDTO.getRedirectLink().isEmpty()) {
                errors.put("redirectLink", "Redirect link is required");
            }

            if (adDTO.getStartDate() == null) {
                errors.put("startDate", "Start date is required");
            }
            if (adDTO.getEndDate() == null) {
                errors.put("EndDate", "End date is required");
            }

            if (adDTO.getCalculatedPrice() == null){
                errors.put("calculatedPrice", "Calculated Price is required");
            }

            if(errors.isEmpty()) {
                savedAd = adRepository.save(newAd);
                AdDTO responseAdDTO = convertToDTO(savedAd);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad created, ready for payment");
                responseDTO.setContent(responseAdDTO);
            } else {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(null);
                responseDTO.setErrors(errors);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(savedAd.getId());
            responseDTO.setErrors(errors);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO confirmAdPayment(String adId, String sessionId) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            Ad ad = adRepository.findById(adId).orElse(null);

            if (ad == null) {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Ad not found");
                return responseDTO;
            }

            ad.setStatus("ACTIVE");
            ad.setCreatedAt(new Date());
            adRepository.save(ad);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad Created successfully");
            return responseDTO;
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error creating ad");
            return responseDTO;

        }
    }


    public ResponseDTO findAdById(String id) {
        Ad ad = adRepository.findById(id).orElse(null);

        if(ad == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
            responseDTO.setContent(null);
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad found");
            responseDTO.setContent(convertToDTO(ad));
        }

        return responseDTO;
    }

    public ResponseDTO findAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDTO> adDTOs = ads.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("All ads");
        responseDTO.setContent(adDTOs);

        return responseDTO;
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
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(myAds);
                responseDTO.setMessage(myAds.isEmpty() ? "No ads found" : "Ads found");

            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error fetching ads");
                responseDTO.setContent(null);
            }
        }

        return responseDTO;
    }

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
            if(ad.getBackgroundImage() != null) {
                File file = new File("public" + ad.getBackgroundImage());
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

        if (!existingAd.getCreator().getId().equals(user.getId())) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("You don't have permission to update this ad");
            return responseDTO;
        }

        // Update fields if they are provided in the DTO
        if (adDTO.getInternalTitle() != null) {
            existingAd.setInternalTitle(adDTO.getInternalTitle());
        }
        if (adDTO.getAdType() != null) {
            existingAd.setAdType(adDTO.getAdType());
        }
        if (adDTO.getBackgroundImage() != null) {
            existingAd.setBackgroundImage(adDTO.getBackgroundImage());
        }
        if (adDTO.getRedirectLink() != null) {
            existingAd.setRedirectLink(adDTO.getRedirectLink());
        }
        if (adDTO.getStartDate() != null) {
            existingAd.setStartDate(adDTO.getStartDate());
        }
        if (adDTO.getCalculatedPrice() != null) {
            existingAd.setCalculatedPrice(adDTO.getCalculatedPrice());
        }

        try {
            Ad updatedAd = adRepository.save(existingAd);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad updated successfully");
            responseDTO.setContent(convertToDTO(updatedAd));
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error updating ad");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    // Helper method to convert Entity to DTO
    private AdDTO convertToDTO(Ad ad) {
        AdDTO dto = new AdDTO();
        dto.setInternalTitle(ad.getInternalTitle());
        dto.setAdType(ad.getAdType());
        dto.setBackgroundImage(ad.getBackgroundImage());
        dto.setRedirectLink(ad.getRedirectLink());
        dto.setStartDate(ad.getStartDate());
        dto.setCalculatedPrice(ad.getCalculatedPrice());
        return dto;
    }
}