package com.nighthawk.aetha_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "subscription_tiers")
public class SubscriptionTiers {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Novel novel;

    private String tier1_name;
    private String tier2_name;
    private String tier3_name;

    private String tier1_description;
    private String tier2_description;
    private String tier3_description;

    private List<String> tier1_features;
    private List<String> tier2_features;
    private List<String> tier3_features;

    //? Duration in weeks
    private int tier1_duration;
    private int tier2_duration;
    private int tier3_duration;

    private Double tier1_price;
    private Double tier2_price;
    private Double tier3_price;
}
