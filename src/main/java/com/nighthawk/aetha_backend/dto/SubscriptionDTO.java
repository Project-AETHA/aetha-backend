package com.nighthawk.aetha_backend.dto;

import lombok.Data;

@Data
public class SubscriptionDTO {

    private String novelId;
    private int subscriptionTier;
    private Double amount;

}
