package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {

    private String id;

    //? Novel details and the user details is unnecessary when we are only showing the chapters
//    @DBRef(lazy = true)
//    private Novel novel;

    private String title;
    private Integer chapterNumber;

    //? Fetching the content is not needed when we are only using ChapterDTO to show the chapters
//    private String content;
    private Double rate;
    private ContentStatus status = ContentStatus.COMPLETED;
    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    private Boolean isVisible = true;
    private Boolean isPremium = false;

}
