package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {

    private String id;

    private NotifyType type;

    private String subject;
    private String message;

    private Boolean seen;
    private String link;

    private String recipient;

}
