package com.nighthawk.aetha_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "favPoem")
@AllArgsConstructor
@NoArgsConstructor
public class FavPoem {

    @Id
    private String id;
    private Poem poem;
    private AuthUser user;

}
