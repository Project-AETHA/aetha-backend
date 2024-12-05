package com.nighthawk.aetha_backend.entity;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("history")
@Builder
public class History {

    @Id
    private String id;

    @DBRef(lazy = true)
    private AuthUser user;

    @DBRef(lazy = true)
    private Novel novel;

    @Builder.Default
    private Date createdAt = new Date();

}
