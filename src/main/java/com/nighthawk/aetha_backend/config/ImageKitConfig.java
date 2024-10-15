package com.nighthawk.aetha_backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
public class ImageKitConfig {

    private final String privateKey;

    @Value("${env.imagekit.publicKey}")
    private String publicKey;

    @Value("${env.imagekit.urlEndpoint}")
    private String urlEndpoint;

    @Autowired
    public ImageKitConfig(Environment env) {
        this.privateKey = env.getProperty("env.imagekit.privateKey");
    }
}