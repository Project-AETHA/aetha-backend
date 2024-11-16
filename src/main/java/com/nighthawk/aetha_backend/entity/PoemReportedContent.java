package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.predefined.ContentType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;


@Data
@Document("reported_novel")
public class PoemReportedContent {

    @Id
    private String id;

    @DocumentReference(collection = "poems")
    private Poem poem;

    //    @DocumentReference(collection = "users")
    @Indexed
    @DBRef(lazy = true)
    private AuthUser user;

    private ContentType type;

    private Date createdAt = new Date();

    private String reason;
}
