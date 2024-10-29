package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Review;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortStoryDTO {
    private String id;
    private AuthUser author;
    private String title;
    private String synopsis;
    private String description;
    private String coverImage;
    private List<String> genres;
    private List<String> tags;
    private List<String> customTags;
    private List<String> contentWarning;
    private Date manualReleaseDate;
    private List<Review> reviews;
    private ContentStatus status;
    private LocalDate createdAt;
    private LocalDate publishedAt;
}
