package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private String id;
    private String title;
    private String content;
    private Date createdAt;
    private Date expiresAt;
    private Boolean isActive;
    private String imageUrl;
    private Double budget;
    private String pricePlan;
    private String campaignType;

    // Creator information
    private String creatorId;
    private String creatorName;

    // Additional fields for analytics (if needed)
    private Integer views;
    private Integer clicks;

    // You can add more fields as needed
}