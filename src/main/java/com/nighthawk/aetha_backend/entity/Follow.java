package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Document("follow")
public class Follow {
    @Id
    private String id;

    @DBRef(lazy = true)
    private AuthUser follower;

    @DBRef(lazy = true)
    private AuthUser following;

    final private LocalDate followedAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}