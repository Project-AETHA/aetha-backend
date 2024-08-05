package com.nighthawk.aetha_backend.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document("notes")
public class Note {

    @Id
    private String id;

    private String title;
    private String content;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser owner;
    private Date lastModified;
    private Date createdAt;
}
