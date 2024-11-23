package com.nighthawk.aetha_backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class ProfileDetails{
    @Id
    private String id;
    private String username;
    private int bday;
    private String joined;
    private String lastActive;
    private String gender;
    private String location;
    private String web;
    private String twitter;
    private String fb;
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

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBday() {
        return bday;
    }

    public void setBday(int bday) {
        this.bday = bday;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getFictions() {
        return fictions;
    }

    public void setFictions(int fictions) {
        this.fictions = fictions;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getTotalReviewsReceived() {
        return totalReviewsReceived;
    }

    public void setTotalReviewsReceived(int totalReviewsReceived) {
        this.totalReviewsReceived = totalReviewsReceived;
    }

    public int getTotalRatingsReceived() {
        return totalRatingsReceived;
    }

    public void setTotalRatingsReceived(int totalRatingsReceived) {
        this.totalRatingsReceived = totalRatingsReceived;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getAuthorFavorites() {
        return authorFavorites;
    }

    public void setAuthorFavorites(int authorFavorites) {
        this.authorFavorites = authorFavorites;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

