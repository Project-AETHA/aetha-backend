package com.nighthawk.aetha_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favNovel")
public class FavNovel {

    @Id
    private String id;

    @DocumentReference(collection = "novels")
    private Novel novel;

    @DocumentReference(collection = "users")
    private AuthUser user;
}
