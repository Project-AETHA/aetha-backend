package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Data
@Document("follow")
public class FollowUser {
    @Id
    private String id;

    @DocumentReference(collection = "users")
    private AuthUser follower;

    @DocumentReference(collection = "users")
    private AuthUser following;

    private LocalDate followedAt;
}