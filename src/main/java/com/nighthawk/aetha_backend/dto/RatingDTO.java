package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatingDTO {
    private String id;
    private Integer rating;
    private String content;
    private String novel;
    private String user;
    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}
