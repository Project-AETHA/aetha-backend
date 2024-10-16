package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import com.nighthawk.aetha_backend.utils.predefined.ContentWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Builder
@Data
@Document("novels")
@AllArgsConstructor
@NoArgsConstructor
public class Novel {

    @Id
    private String id;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser author;

    @Indexed
    private String title;

    private String synopsis;
    private String description;
    private String coverImage;

    private List<Genres> genres;
    private List<Tags> tags;
    private List<String> customTags;

    // ? Not sure of the content
    private List<ContentWarnings> contentWarning;
    private Date manualReleaseDate;
    private List<Review> reviews;

    //? Default status is PENDING, after admin approval the status will be changed to PUBLISHED
    private ContentStatus status = ContentStatus.PENDING;
    private LocalDate createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    //? Published datetime will be added when the admin has approved and published the novel
    private LocalDate publishedAt;
}
