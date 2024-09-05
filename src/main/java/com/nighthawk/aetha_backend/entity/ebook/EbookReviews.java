package com.nighthawk.aetha_backend.entity.ebook;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document("ebook_reviews")
public class EbookReviews {

    @Id
    private String id;

    @DocumentReference(collection = "ebooks_external")
    @Indexed
    private String ebook_id;

    @DocumentReference(collection = "users")
    @Indexed
    private String user_id;

    // ? Text containing the user's review
    private String review;


    // ? Ratings for different aspects of the book
    private double rating_overall;
    private double rating_plot;
    private double rating_characters;
    private double rating_writing;
    private double rating_diversity;

    private Date createdAt;
    private Date updatedAt;
}
