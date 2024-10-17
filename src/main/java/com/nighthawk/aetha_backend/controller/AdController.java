package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.AdDTO;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAd(
            @RequestBody AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.createAd(adDTO, userDetails), HttpStatus.OK);
    }

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

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllAds() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("All ads");
        responseDTO.setContent(adService.findAllAds());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/my-ads")
    public ResponseEntity<ResponseDTO> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adService.findMyAds(userDetails));
    }

//    @PostMapping("/search")
//    public ResponseEntity<ResponseDTO> searchAds(@RequestBody RequestDTO requestDTO) {
//        responseDTO.setCode(VarList.RSP_SUCCESS);
//        responseDTO.setMessage("Search results");
//        responseDTO.setContent(adService.searchAds(requestDTO));
//
//        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//    }

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
            @ModelAttribute AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(adService.updateAd(id, adDTO, userDetails), HttpStatus.OK);
    }
}