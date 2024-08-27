package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Tags;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class EbookExternalDTO {

    private String id;
    private String title;
    private String description;
    private String isbn;
    private List<Genres> genres;
    private List<Tags> tags;
    private List<String> custom_tags;
    private String author;
    private String cover_image;
    private Date createdAt;
    private Double price;
    private Integer sold_amount = 0;
    private Integer rating;
    private Integer views;
}