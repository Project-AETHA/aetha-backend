package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.service.AdService;
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
    private AdService adService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAd(
            @RequestBody Ad ad,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.createAd(ad, userDetails), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getAdById(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.findAdById(id, userDetails), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllAds(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.findAllAds(userDetails), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateAd(
            @RequestBody Ad ad,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ) {
        return new ResponseEntity<>(adService.updateAd(id, ad, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAd(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.deleteAd(id, userDetails), HttpStatus.OK);
    }
}