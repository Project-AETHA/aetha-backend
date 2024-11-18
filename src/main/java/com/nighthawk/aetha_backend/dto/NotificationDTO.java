package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {

    @Id
    private String id;

    private NotifyType type;
    private NotificationCategory category = NotificationCategory.GENERAL;
    private String subject;
    private String message;
    private Boolean seen = false;
    private String link;

    @DocumentReference(collection = "users")
    @Indexed
    private AuthUser recipient;

    @CreatedDate
    private Date createdAt = new Date();

}
