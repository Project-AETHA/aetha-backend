package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDTO {
    private String id;
    private String content;
    private String novelId;
    private String userId;
    private String createdAt;
}
