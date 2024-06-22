package com.nighthawk.aetha_backend.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private String name;

    private String image;

    private String genre;

    private List<Review> reviews;

    private String description;

    private String isbn;

}
