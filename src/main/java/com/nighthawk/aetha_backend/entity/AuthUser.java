package com.nighthawk.aetha_backend.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document("users")
public class AuthUser {

    @Id
    private String id;
    @Indexed
    private String username;
    private String displayName;
    private String email;
    private String firstname;
    private String lastname;
    private String image;
    private String gender;
    private String password;
    private Date birthdate;
    private Role role;
    private AccStatus status;

}
