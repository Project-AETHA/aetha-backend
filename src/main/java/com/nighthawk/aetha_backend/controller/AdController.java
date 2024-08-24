package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.service.AdService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private AdService adService;

    // Create an Ad
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAd(
            @RequestBody Ad ad,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Ad savedAd = adService.createAd(ad, userDetails);

        if(savedAd == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Failed to create ad");
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad created successfully");
            responseDTO.setContent(savedAd);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Get an Ad by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getAdById(@PathVariable String id) {
        Ad ad = adService.findAdById(id);

        if(ad == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ad found");
            responseDTO.setContent(ad);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Get all Ads
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllAds() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("All ads");
        responseDTO.setContent(adService.findAllAds());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update an Ad
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateAd(
            @RequestBody Ad ad,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ) {
        Ad existingAd = adService.findAdById(id);

        if(existingAd == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
        } else {
            Ad updatedAd = adService.updateAd(id, ad, userDetails);

            if(updatedAd != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad updated successfully");
                responseDTO.setContent(updatedAd);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to update ad");
            }
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete an Ad
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAd(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Ad ad = adService.findAdById(id);

        if(ad == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Ad not found");
        } else {
            Boolean deleted = adService.deleteAd(ad, userDetails);

            if(deleted) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ad deleted successfully");
                responseDTO.setContent(ad);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to delete ad");
            }
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
