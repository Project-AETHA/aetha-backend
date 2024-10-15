package com.nighthawk.aetha_backend.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Builder
@Data
@Document("novels")
public class Novel {

    @Id
    private String id;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser author;

    @Indexed
    private String title;

    private String synopsis;
    private String description;
    private String cover_image;

    private List<Genres> genre;
    private List<String> tags;
    private List<String> custom_tags;

    // ? Not sure of the content
    private List<String> content_warning;

    private Date manual_release_date;

    private List<Review> reviews;

    private String status;

    private Date created_At;

    private Date published_At;
}
