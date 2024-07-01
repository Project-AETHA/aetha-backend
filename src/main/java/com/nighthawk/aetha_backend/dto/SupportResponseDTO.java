package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.utils.StatusList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupportResponseDTO {

    private String id;
    private UserDTO author;
    private String title;
    private String category;
    private String description;
    private Date createdAt;
    private List<String> attachments;
    private StatusList status;
    private UserDTO handledBy;

}
