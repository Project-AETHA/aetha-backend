package com.nighthawk.aetha_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());

    @PostMapping("/create")
    public boolean createNotification() {
        try {
            log.info("Creating a notification");
            System.out.println("Creating a notification");
            //? Create a notification

            logger.log(Level.SEVERE, "error log");
            logger.log(Level.WARNING, "warning log");
            logger.log(Level.INFO, "info log");
            logger.log(Level.FINE, "debug log");
            logger.log(Level.FINER, "trace log");

//            throw new Exception("Unimplemented Method");
            //?  On success returns true
            return true;
        } catch (Exception e) {
            log.error("Error creating a notification - {}", e.getMessage());
            System.out.println("Error creating a notification");
            return false;
        }
    }

}
