package com.nighthawk.aetha_backend.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("reviews")
public class Review {

    @Id
    private String id;

    private double rating_overall;
    private double rating_plot;
    private double rating_characters;
    private double rating_writing;
    private double rating_diversity;

    private String comment;

}
