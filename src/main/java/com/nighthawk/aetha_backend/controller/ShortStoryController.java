package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ShortStoryDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.ShortStory;
import com.nighthawk.aetha_backend.service.ShortStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/shortstory")
public class ShortStoryController {

    private ResponseDTO response;

    @Autowired
    ShortStoryService ShortStoryService;


    // TODO - moved on to finalizing the eBook module


    //? Get ShortStory by ShortStory ID
    @GetMapping("/{ShortStoryId}")
    public ResponseEntity<ResponseDTO> getShortStoryById(@PathVariable String ShortStoryId) {
        return ResponseEntity.ok(ShortStoryService.getShortStoryById(ShortStoryId));
    }

    // ? Creating a ShortStory
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createShortStory(@RequestBody ShortStoryDTO ShortStoryDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ShortStoryService.createShortStory(ShortStoryDTO, userDetails));
    }

    // ? Update a ShortStory
    // ! Should include a pre-check of the subscription count, if the content is updated
    @PatchMapping("/update/{ShortStoryId}")
    public ResponseEntity<ResponseDTO> updateShortStory(@RequestBody ShortStory ShortStory, @PathVariable String ShortStoryId) {
        return ResponseEntity.ok(ShortStoryService.updateShortStory(ShortStory));
    }

    // ? Deleting a ShortStory
    // ! Should include a pre-check about any subscriptions before deleting a book
    @DeleteMapping("/delete/{ShortStoryId}")
    public ResponseEntity<ResponseDTO> deleteShortStory(@PathVariable String ShortStoryId) {
        return ResponseEntity.ok(ShortStoryService.deleteShortStory(ShortStoryId));
    }


    // ? Get all ShortStorys
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllShortStorys() {
        return ResponseEntity.ok(ShortStoryService.getAllShortStorys());
    }

    //? Get all ShortStorys paginated
    @GetMapping("/all/paginated")
    public ResponseEntity<ResponseDTO> getAllShortStorysPaginated(
            @RequestBody RequestDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(ShortStoryService.getAllShortStorysPaginated(page, pageSize));
    }

    //? Get my ShortStorys
    @GetMapping("/my")
    public ResponseEntity<ResponseDTO> getMyShortStorys(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ShortStoryService.getMyShortStorys(userDetails));
    }

    // ? Search by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<ResponseDTO> getShortStorysByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(ShortStoryService.getShortStorysByAuthor(authorId, page, pageSize));
    }


    //!!! Search results for ShortStorys
    // TODO - Implement the search functionality
    @PostMapping("/search")
    public ResponseEntity<ResponseDTO> searchShortStorys(
            @RequestBody RequestDTO requestDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
//        return ResponseEntity.ok(ShortStoryService.searchShortStorys(requestDTO, page, pageSize));
        return null;
    }

    // ? Search by title
    @PostMapping("/filter")
    public ResponseEntity<ResponseDTO> getShortStoryByTitle(
            @RequestBody RequestDTO requestDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int pageSize
    ) {
        return ResponseEntity.ok(ShortStoryService.filterShortStorys(requestDTO, page, pageSize));
    }


    //? Approving a ShortStory by the admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{ShortStoryId}")
    public ResponseEntity<ResponseDTO> approveShortStory(@PathVariable String ShortStoryId) {
        return ResponseEntity.ok(ShortStoryService.approveShortStory(ShortStoryId));
    }

    //? Rejecting a ShortStory by the admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{ShortStoryId}")
    public ResponseEntity<ResponseDTO> rejectShortStory(@PathVariable String ShortStoryId) {
        return ResponseEntity.ok(ShortStoryService.rejectShortStory(ShortStoryId));
    }

}
