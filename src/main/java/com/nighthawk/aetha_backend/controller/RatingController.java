package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.RatingDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.RatingService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin
public class RatingController {

    private final RatingService ratingService;
    private final ResponseDTO responseDTO;

    public RatingController(RatingService ratingService, ResponseDTO responseDTO) {
        this.ratingService = ratingService;
        this.responseDTO = responseDTO;
    }

    @GetMapping("/getRating")
    public ResponseEntity<ResponseDTO> getRating() {
        return new ResponseEntity<>(ratingService.getRating(), HttpStatus.OK);
    }

    @PostMapping("/saveRating")
    public ResponseEntity<ResponseDTO> saveRating(@RequestBody RatingDTO ratingDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO response = new ResponseDTO();
        try {
            response = ratingService.saveRating(ratingDTO, userDetails);
        } catch (Exception e) {
            response.setCode(VarList.RSP_ERROR);
            response.setMessage("Error saving rating");
            response.setContent(null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
