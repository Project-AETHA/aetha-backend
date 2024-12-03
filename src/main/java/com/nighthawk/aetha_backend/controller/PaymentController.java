package com.nighthawk.aetha_backend.controller;
import com.nighthawk.aetha_backend.dto.PaymentRequestDTO;
import com.nighthawk.aetha_backend.dto.PaymentResponseDTO;
import com.nighthawk.aetha_backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponseDTO> checkoutPayments(
            @RequestBody PaymentRequestDTO paymentRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PaymentResponseDTO paymentResponse = paymentService.checkoutPayments(paymentRequest, userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentResponse);
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
