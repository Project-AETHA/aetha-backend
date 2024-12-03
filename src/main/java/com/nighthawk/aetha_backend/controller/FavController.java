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

    //? Add a poem to favourites
    @PostMapping("/poem/{poemId}")
    public ResponseEntity<ResponseDTO> addFavPoem(
            @PathVariable String poemId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean setFav
    ) {
        return ResponseEntity.ok(favService.addFavPoem(poemId, userDetails, setFav));
    }

    //? Add a novel to favourites
    @PostMapping("/novel/{novelId}")
    public ResponseEntity<ResponseDTO> addFavNovel(
            @PathVariable String novelId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean setFav
    ) {
        return ResponseEntity.ok(favService.addFavNovel(novelId, userDetails, setFav));
    }

    //? Check if the poem is a favourite
    @GetMapping("/poem/{poemId}")
    public ResponseEntity<ResponseDTO> isFavPoem(
            @PathVariable String poemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(favService.isFavPoem(poemId, userDetails));
    }

    //? Check if the novel is a favourite
    @GetMapping("/novel/{novelId}")
    public ResponseEntity<ResponseDTO> isFavNovel(
            @PathVariable String novelId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(favService.isFavNovel(novelId, userDetails));
    }

    //? Check if the poem is a favourite
    @GetMapping("/poems")
    public ResponseEntity<ResponseDTO> getMyFavPoems(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favService.getMyFavPoems(userDetails));
    }

    //? Check if the novel is a favourite
    @GetMapping("/novels")
    public ResponseEntity<ResponseDTO> getMyFavNovels(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favService.getMyFavNovels(userDetails));
    }

}
