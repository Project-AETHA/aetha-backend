package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.FavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/fav")
public class FavController {

    private final FavService favService;

    @Autowired
    public FavController(
            FavService favService
    ) {
        this.favService = favService;
    }

    @PostMapping("/poem/{poemId}")
    public ResponseEntity<ResponseDTO> addFavPoem(
            @PathVariable String poemId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean setFav
    ) {
        return ResponseEntity.ok(favService.addFavPoem(poemId, userDetails, setFav));
    }

}
