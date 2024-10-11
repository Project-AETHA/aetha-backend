package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document("upvote_records")
public class VoteRecords {

    @Id
    private String id;

    @DocumentReference(collection = "users")
    private AuthUser user;

    @DocumentReference(collection = "poems")
    private Poem poem;

    private Date datetime;

    private Boolean isupvote;

}
