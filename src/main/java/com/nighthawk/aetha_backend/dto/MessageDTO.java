package com.nighthawk.aetha_backend.dto;

import java.util.Date;

public class MessageDTO {

    private String title;
    private String sender;
    private String receiver;
    private Date dateTime;

    // Constructors, getters, and setters

    public MessageDTO() {}

    public MessageDTO(String title, String sender, String receiver, Date dateTime) {
        this.title = title;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
    }


    // Getters and Setters
    // ...
}
