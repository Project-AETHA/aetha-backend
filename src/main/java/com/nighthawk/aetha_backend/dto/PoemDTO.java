package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PoemDTO {
    private String id;
    private AuthUser author;
    private String title;
    private List<String> genres;
    private String content;
    private Date created_At;
    private String image;
    private Integer views;
    private Integer clicks;
    private Boolean visibility;
    private List<String> comments;
    private Integer upvotes;
    private Integer downvotes;
}
