package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.FollowUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/following")
public class FollowUserController {

    private final FollowUserService followUserService;

    @Autowired
    public FollowUserController(FollowUserService followUserService) {
        this.followUserService = followUserService;
    }

    //? Follow a user
    @GetMapping("/follow/{followingUserId}")
    public ResponseEntity<ResponseDTO> followUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String followingUserId
            ) {
        return ResponseEntity.ok(followUserService.followAUser(userDetails, followingUserId));
    }

    //? Unfollow a user
    @GetMapping("/unfollow/{followingUserId}")
    public ResponseEntity<ResponseDTO> unfollowUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String followingUserId
            ) {
        return ResponseEntity.ok(followUserService.unfollowAUser(userDetails, followingUserId));
    }

    //? Get the following list of currently logged-in user
    @GetMapping("/following-list")
    public ResponseEntity<ResponseDTO> getFollowingList(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return ResponseEntity.ok(followUserService.getFollowingList(userDetails));
    }

    //? Get the followers list of currently logged-in user
    @GetMapping("/followers-list")
    public ResponseEntity<ResponseDTO> getFollowersList(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return ResponseEntity.ok(followUserService.getFollowersList(userDetails));
    }

    //? Get follower and following count
    @GetMapping("/count")
    public ResponseEntity<ResponseDTO> getFollowerFollowingCount(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return ResponseEntity.ok(followUserService.getFollowersAndFollowingCount(userDetails));
    }

}
