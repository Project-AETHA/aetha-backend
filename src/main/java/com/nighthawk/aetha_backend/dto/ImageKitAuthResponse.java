package com.nighthawk.aetha_backend.dto;

import lombok.Data;

@Data
public class ImageKitAuthResponse {
    private String token;
    private long expire;
    private String signature;

    public ImageKitAuthResponse(String token, long expire, String signature) {
        this.token = token;
        this.expire = expire;
        this.signature = signature;
    }
}
