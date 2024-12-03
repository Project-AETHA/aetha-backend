package com.nighthawk.aetha_backend.controller;

<<<<<<< HEAD
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
=======
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

>>>>>>> 75edaf93d9018d29155748b34383571866d9d258
}
