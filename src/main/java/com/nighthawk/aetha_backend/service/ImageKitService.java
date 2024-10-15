package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.config.ImageKitConfig;
import com.nighthawk.aetha_backend.dto.ImageKitAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageKitService {

    @Autowired
    private ImageKitConfig imageKitConfig;

    public ImageKitAuthResponse getAuthenticationParameters() {
        try {
            String token = UUID.randomUUID().toString();
            long expire = System.currentTimeMillis() / 1000L + 1800; // 30 minutes expiry

            String signature = generateSignature(token, expire);

            return new ImageKitAuthResponse(token, expire, signature);
        } catch (Exception e) {
            throw new RuntimeException("Error generating authentication parameters", e);
        }
    }

    private String generateSignature(String token, long expire) throws NoSuchAlgorithmException, InvalidKeyException {
        String rawSignature = token + expire;
        SecretKeySpec secretKeySpec = new SecretKeySpec(imageKitConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        byte[] hmacSha1 = mac.doFinal(rawSignature.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hmacSha1) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}