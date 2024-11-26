package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.SubscriptionDTO;
import com.nighthawk.aetha_backend.service.SubscriptionService;
import com.nighthawk.aetha_backend.service.SubscriptionTiersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService _subscriptionService;
    private final SubscriptionTiersService _subscriptionTiersService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionTiersService subscriptionTiersService) {
            _subscriptionService = subscriptionService;
            _subscriptionTiersService = subscriptionTiersService;
    }

    //? Creating a new subscription
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createSubscription(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SubscriptionDTO subscriptionDTO
            ) {
        return ResponseEntity.ok(_subscriptionService.createSubscription(userDetails, subscriptionDTO));
    }

    @GetMapping("/current")
    public ResponseEntity<ResponseDTO> getYourCurrentSubscriptions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(_subscriptionService.getCurrentSubscriptions(userDetails));
    }

    @GetMapping("/get-tiers/{novelId}")
    public ResponseEntity<ResponseDTO> getSubscriptionTiers(
            @PathVariable String novelId
    ) {
        return ResponseEntity.ok(_subscriptionTiersService.getSubscriptionTiersForNovel(novelId));
    }

}
