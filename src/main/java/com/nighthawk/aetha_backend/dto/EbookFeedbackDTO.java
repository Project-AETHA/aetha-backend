package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EbookFeedbackDTO {

    @Id
    private String id;
    private AuthUser author;
    private String comment;
    private double rating;

}
