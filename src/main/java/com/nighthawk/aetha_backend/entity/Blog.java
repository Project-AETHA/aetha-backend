package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("blogs")
public class Blog {

    @Id
    private String id;
    @Indexed
    private String title;
    private String body;
    @Indexed
    private String author;

}
