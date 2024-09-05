package com.nighthawk.aetha_backend.entity.ebook;

import com.nighthawk.aetha_backend.dto.EbookFeedbackDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Tags;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Data
@Document("ebooks_external")
public class EbookExternal {

    @Id
    private String id;
    private String title;
    private String description;
    private String isbn;
    private List<Genres> genres;
    private List<Tags> tags;
    private List<String> custom_tags;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser author; //? In the EbookExternal, only the displayName of the user is sent

    private String cover_image;
    private String demo_loc;
    private String original_loc;
    private Date createdAt;
    private Double price;
    private Integer sold_amount = 0;
    private List<EbookReviews> reviews;

    // ? Overall rating of all the current reviews and total of all the views
    private Integer rating;
    private Integer views;

    private List<EbookFeedbackDTO> feedback; //? This won't be included in the EBookExternalDTO

    private ContentStatus status;
}
