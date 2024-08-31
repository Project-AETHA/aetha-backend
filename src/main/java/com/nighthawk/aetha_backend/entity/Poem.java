package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

// ? -----Data-----
// ? Data annotation eka use karanne
// ? setters and getters auto create karanna ( hama ekatama create wela thiyenwa Data kiyana eka dammama )
// ? Data eka naththam wenama hadanna oona getters and setters private attributes walata

// ? -----Document-----
// ? Collection name-poems
// ? Collection eke achchuwa hadenne entity ekata anuwa
// ? Service eken data ekak save karanna kiwwama, repository eka entity kiyana achchuwa
// ? use karalai database ekata save karanne, ethakota database ekata achchuwe thiyena ewagen pita ewa yanne na
@Data
@Document("poems")
public class Poem {

    @Id // ? primary key
    private String id;

    @DocumentReference(collection = "users") // ? Relation to users collection
    @Indexed // ? Data set eka lokunm index pawichchi kraoth search eka speed
    private AuthUser author;

    @Indexed
    private String title;

    private List<String> tags;

    private String content;

    private Date created_At;

    private String image;

    private Integer views;

    private Integer clicks;

    private Boolean visibility;

    private List<String> comments;

    private Integer upvotes;




}
