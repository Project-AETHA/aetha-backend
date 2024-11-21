package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForumDTO {
    private String id;
    private String topic;
    private String content;
    private String user;
    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}
