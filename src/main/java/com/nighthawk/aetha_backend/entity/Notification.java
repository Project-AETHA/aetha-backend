package com.nighthawk.aetha_backend.entity;

import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Document(collection = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private String id;

    private NotifyType type;
    private NotificationCategory category = NotificationCategory.GENERAL;
    private String subject;
    private String message;
    private Boolean seen;
    private String link;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser recipient;

    @CreatedDate
    private Date createdAt;

}
