package com.nighthawk.aetha_backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    private String sender;
    private String receiver;
    private String title;
    private String content;
    private Date dateTime;
    private MessageStatus status;

    // Constructors, getters, and setters

    public Message() {}

    public Message(String sender, String receiver, String title, String content, Date dateTime, MessageStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.status = status;
    }

    // Getters and Setters
    // ...
}
