package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
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
    private List<String> custom_tags;
    private Integer sold_amount;
    private Date createdAt;
    private Date updatedAt;

    // ? Specific to Content
    private Date manual_release_date;
}
