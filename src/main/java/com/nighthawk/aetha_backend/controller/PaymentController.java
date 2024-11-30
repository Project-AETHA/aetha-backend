package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.StripeRequest;
import com.nighthawk.aetha_backend.dto.StripeResponse;
import com.nighthawk.aetha_backend.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutItem(@RequestBody StripeRequest request) {
        return ResponseEntity.ok(stripeService.checkoutItem(request));
    }

    //? Replace with payment success frontend page
    @GetMapping("/success")
    public ResponseEntity<String> success() {
        return ResponseEntity.ok("Payment successful");
    }

    //? Replace with payment cancel frontend page
    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return ResponseEntity.ok("Payment cancelled");
    }

}
