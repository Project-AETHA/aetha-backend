package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Data
@Document("ads")
public class Ad {

    @Id
    private String id;
    private String title;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser creator;

    private String content;
    private Date createdAt;
    private Date expiresAt;
    private Boolean isActive;
    private String imageUrl;
    private Double budget;
    private String pricePlan;
    private String campaignType;

    // Additional fields for tracking metrics
    private Integer impressions = 0;       // Number of times the ad has been viewed
    private Integer clicks = 0;            // Number of times the ad has been clicked
    private Double adSpend = 0.0;          // Total money spent on this ad
    private Double costPerClick = 0.0;     // Cost per Click (CPC)
    private Double costPerThousand = 0.0;  // Cost per Thousand Impressions (CPM)
    private Double clickThroughRate = 0.0; // Click-Through Rate (CTR)
    private Double returnOnAdSpend = 0.0;  // Return on Ad Spend
    private Double conversionRate = 0.0;   // Conversion Rate (percentage of clicks resulting in a conversion)
    private Integer conversions = 0;       // Total number of conversions
    private Double revenueGenerated = 0.0; // Total revenue generated from this ad
    private Integer bounceRate = 0;        // Percentage of users who clicked but did not take further action

    // Additional Fields for Ad Settings and Management

    private String preview;                // Ad preview URL or data (optional)
    private String description;            // Description of the ad campaign (optional)
    private String category;               // Ad category (e.g., "Technology", "Lifestyle")
    private Boolean isValidUpload;         // Image or Text validation (for background uploads)

    // Constructors, getters, and setters can be generated automatically by Lombok
}
