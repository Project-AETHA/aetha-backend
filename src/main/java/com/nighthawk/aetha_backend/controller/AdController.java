package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.AdDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
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
            @RequestBody AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.createAd(adDTO, userDetails), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getAdById(@PathVariable String id) {
        return new ResponseEntity<>(adService.findAdById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllAds() {
        return new ResponseEntity<>(adService.findAllAds(), HttpStatus.OK);
    }

    @GetMapping("/my-ads")
    public ResponseEntity<ResponseDTO> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adService.findMyAds(userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAd(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.deleteAd(id, userDetails), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateAd(
            @PathVariable String id,
            @RequestBody AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.updateAd(id, adDTO, userDetails), HttpStatus.OK);
    }
}