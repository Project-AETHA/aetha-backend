package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.PaymentRequestDTO;
import com.nighthawk.aetha_backend.dto.PaymentResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public PaymentResponseDTO checkoutPayments(PaymentRequestDTO paymentRequestDTO, UserDetails userDetails) {
        Stripe.apiKey = secretKey;

        // Log or verify the authenticated user's details
        String authenticatedUsername = userDetails.getUsername();
        System.out.println("Processing payment for user: " + authenticatedUsername);

        // Create product data
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequestDTO.getName())
                        .build();

        // Create price data
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequestDTO.getCurrency() != null ? paymentRequestDTO.getCurrency() : "USD")
                        .setUnitAmount(paymentRequestDTO.getAmount())
                        .setProductData(productData)
                        .build();

        // Create line item
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(paymentRequestDTO.getQuantity())
                        .setPriceData(priceData)
                        .build();

        // Create session
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();

        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error creating Stripe session: " + e.getMessage());
        }

        // Return payment response
        return PaymentResponseDTO.builder()
                .status("SUCCESS")
                .message("Payment session created for user: " + authenticatedUsername)
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
