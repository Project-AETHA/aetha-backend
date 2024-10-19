package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Builder
@Document("chapters")
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    private String id;

    //? With the below annotation, the entire novel will not be fetched when the chapter is fetched
    @DBRef(lazy = true)
    private Novel novel;
    private String title;
    private Integer chapterNumber;
    private String content;
    private Double rate;
    private ContentStatus status = ContentStatus.COMPLETED;

    @CreatedDate
    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    private Boolean isVisible = true;

}
