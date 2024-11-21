package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.StatusList;
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
public class NovelReportedContent {

    @Id
    private String id;

    @DocumentReference(collection = "novels")
    private Novel novel;

    @DocumentReference(collection = "chapters")
    private String chapterId;

    //    @DocumentReference(collection = "users")
    @Indexed
    @DBRef(lazy = true)
    private AuthUser reporteduser;

    private ContentType type;

    private Date createdAt = new Date();

    private String reason;

    private StatusList status = StatusList.ACTIVE;

}
