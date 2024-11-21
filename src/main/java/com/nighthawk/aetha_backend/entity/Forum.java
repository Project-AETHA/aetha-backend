package com.nighthawk.aetha_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Document("forum")
@NoArgsConstructor
@AllArgsConstructor

public class Forum {
    @Id
    private String id;
    private String topic;
    private String content;
    
    @DBRef(lazy = true)
    private AuthUser user;

    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}

