package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

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

    // Additional fields if necessary
    private Integer impressions = 0;  // Number of times the ad has been viewed
    private Integer clicks = 0;       // Number of times the ad has been clicked

    // Constructors, getters, and setters can be generated automatically by Lombok
}
