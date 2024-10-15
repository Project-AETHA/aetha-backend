package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ImageKitAuthResponse;
import com.nighthawk.aetha_backend.service.ImageKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@CrossOrigin
@RestController
@RequestMapping("/api/imagekit")
public class ImageKitController {

    @Autowired
    private ImageKitService imageKitService;

    @GetMapping("/auth")
    public ImageKitAuthResponse getAuthenticationParameters() throws NoSuchAlgorithmException {
        return imageKitService.getAuthenticationParameters();
    }

}
