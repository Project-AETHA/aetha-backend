package com.nighthawk.aetha_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Document("comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
        @Id
        private String id;
        private String content;

        @DocumentReference(collection = "chapters")
        private Chapter chapter;

        @DocumentReference(collection = "users")
        private AuthUser user;

        private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}
