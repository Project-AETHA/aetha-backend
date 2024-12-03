package com.nighthawk.aetha_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document(collection = "favPoem")
@AllArgsConstructor
@NoArgsConstructor
public class FavPoem {

    @Id
    private String id;

    @DocumentReference(collection = "poems")
    private Poem poem;

    @DocumentReference(collection = "users")
    private AuthUser user;

}
