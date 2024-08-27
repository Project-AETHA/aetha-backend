package com.nighthawk.aetha_backend.entity.ebook;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document("ebook_views")
public class EbookViews {

    @Id
    private String id;

    @DocumentReference(collection = "ebooks_external")
    @Indexed
    private String ebook_id;

    @DocumentReference(collection = "users")
    @Indexed
    private String user_id;

    private Date viewedAt;

}
