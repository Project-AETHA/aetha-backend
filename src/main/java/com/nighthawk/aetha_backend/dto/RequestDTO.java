package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String title;
    private String category;
    private String description;
    private UserDetails userDetails;
    private String id;
    private String isbn;
    private String email;
    private Double price;
    private Integer sold_amount;
    private Date createdAt;
    private Date updatedAt;
}
