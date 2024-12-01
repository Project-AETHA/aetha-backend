package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDTO {
    private String id;
    private AuthUser author;
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
    private Date paymentDate;
}
