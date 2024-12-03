package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.StripeRequest;
import com.nighthawk.aetha_backend.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;
    
    public StripeResponse checkoutItem(StripeRequest request) {
        
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(request.getName()).build();
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .setUnitAmount(request.getAmount())
                .setProductData(productData)
                .build();
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(request.getQuantity())
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/api/payment/success")
                .setCancelUrl("http://localhost:8080/api/payment/cancel")
                .addLineItem(lineItem)
                .build();

        Session session = null;

        try {
            session = Session.create(params);
            return StripeResponse.builder()
                    .status("Success")
                    .message("Session Creation Successful")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            System.out.println("Stripe Error : " + e.getMessage());
            return StripeResponse.builder()
                    .status("Failure")
                    .message("Session Creation Failed")
                    .build();
        }
    }

}
