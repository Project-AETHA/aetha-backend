package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {

    private String id;

    private NotifyType type;
    private NotificationCategory category = NotificationCategory.GENERAL;
    private String subject;
    private String message;

    private Boolean seen;
    private String link;

    private String recipient;

    @CreatedDate
    private Date createdAt;

}
