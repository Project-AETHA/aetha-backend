package com.nighthawk.aetha_backend.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

public class Payment {
    private String id;
    private AuthUser author;
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
    private Date paymentDate;
    private String adId;
    private String internalTitle;
    private String status;
    private String message;
    private String sessionId;
    private  String sessionUrl;


}
