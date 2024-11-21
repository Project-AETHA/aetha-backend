package com.nighthawk.aetha_backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Document(collection = "profile_reviews") // Specifies the MongoDB collection name
public class ProfileReview {
    @Id
    private String id; // Use String for MongoDB IDs (ObjectId in MongoDB)

    private String username;

    private String review;

    private String profilePicUrl;

    private LocalDateTime createdDate = LocalDateTime.now(); // Auto-set when the object is created
}
