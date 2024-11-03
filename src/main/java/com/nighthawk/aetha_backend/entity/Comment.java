package com.nighthawk.aetha_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document("comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
        @Id
        private String id;
        private String content;
        @DocumentReference(collection = "novels")
        private Novel novel;
        @DocumentReference(collection = "users")
        private AuthUser user;
        private Date createdAt;
}
