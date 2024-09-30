package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.utils.predefined.NotifyType;

public class NotificationDTO {

    private String id;

    private NotifyType type;

    private String subject;
    private String message;

    private Boolean seen;
    private String link;

    private String recipient;

}
