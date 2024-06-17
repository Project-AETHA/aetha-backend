package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document("blogs")
public class Blog {

    @Id
    private String id;
    @Indexed
    private String title;
    private String body;
    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser author;

}
