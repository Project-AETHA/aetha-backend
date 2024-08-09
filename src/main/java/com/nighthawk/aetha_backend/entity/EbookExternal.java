package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.dto.EbookFeedbackDTO;
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
    private String isbn;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser author;

    private String cover_image;
    private String demo_loc;
    private String original_loc;
    private Date createdAt;
    private Double price;
    private Integer sold_amount = 0;
    private List<EbookFeedbackDTO> feedback;

}
