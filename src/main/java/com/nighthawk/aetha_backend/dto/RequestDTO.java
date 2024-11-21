package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private String title;
    private List<String> genres;
    private String description;
    private UserDetails userDetails;
    private String id;
    private String isbn;
    private String email;
    private String price;
    private List<String> tags;
    private List<String> customTags;
    private Integer soldAmount;
    private Date createdAt;
    private Date updatedAt;
    private String searchTerm;
    private Double rating;
    private String publishedWithin;

    // ? Specific to Content
    private Date manualReleaseDate;

    //? Comment
    private String novel;
    private String user;
}
