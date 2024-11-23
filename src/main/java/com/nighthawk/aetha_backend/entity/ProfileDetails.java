package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "users")
public class ProfileDetails{
    @Id
    private String id;
    private String username;
    private LocalDate bday;
    private String joined;
    private String lastActive;
    private String gender;
    private String location;
    private String web;
    private String twitter;
    private String facebook;
    private String bio;
    private int follows;
    private int favorites;
    private int ratings;
    private int reviews;
    private int comments;
    private int fictions;
    private int totalWords;
    private int totalReviewsReceived;
    private int totalRatingsReceived;
    private int followers;
    private int authorFavorites;
    private String image;
}

