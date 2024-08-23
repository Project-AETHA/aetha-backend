package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Genres;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String title;
    private List<Genres> genres;
    private String description;
    private UserDetails userDetails;
    private String id;
    private String isbn;
    private String email;
    private String price;
    private List<String> tags;
    private List<String> customTags;
    private Integer sold_amount;
    private Date createdAt;
    private Date updatedAt;
}
