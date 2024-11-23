package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.predefined.SubscriptionStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Data
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    @DocumentReference(collection = "users")
    private AuthUser user;

    @DocumentReference(collection = "novels")
    private Novel novel;

    @DocumentReference(collection = "subscription_tiers")
    private SubscriptionTiers subscriptionTier; //? From 1 , 2, or 3
    private LocalDate startedData;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private Double amount; //? Amount paid for the subscription, can be removed once payment is implemented

    //? Include the transaction details
    // private Payment payment;

}
