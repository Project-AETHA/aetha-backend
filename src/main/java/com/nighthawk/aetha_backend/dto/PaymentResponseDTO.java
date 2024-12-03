package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class PaymentResponseDTO {
    private String status;
    private String message;
    private String sessionId;
    private  String sessionUrl;
}
