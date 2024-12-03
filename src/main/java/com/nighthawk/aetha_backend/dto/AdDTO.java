package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private String internalTitle;
    private String adType;
    private String backgroundImage;
    private String redirectLink;
    private Date startDate;
    private Date endDate;
    private Long calculatedPrice;

    // Creator information
    private String creatorId;
    private String creatorName;

    // Additional fields for analytics (if needed)
    private Integer views;
    private Integer clicks;

    // You can add more fields as needed
}