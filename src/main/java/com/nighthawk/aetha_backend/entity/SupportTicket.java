package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.StatusList;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Data
@Document("support_tickets")
public class SupportTicket {

    @Id
    private String id;

    @NotNull
    @DocumentReference(collection = "users")
    private AuthUser author;

    @NotNull
    private String title;

    @NotNull
    private String category;
    private String description;

    private Date createdAt;

    private List<String> attachments;

    private StatusList status = StatusList.PENDING;

    @DocumentReference(collection = "users")
    private AuthUser handledBy;

}
