package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ebook_records")
public class EbookRecord {

    @Id
    private String id;

    @DocumentReference(collection = "ebooks_external")
    private EbookExternal ebook;

    @DocumentReference(collection = "users")
    private AuthUser user;

    private long amount;
}
