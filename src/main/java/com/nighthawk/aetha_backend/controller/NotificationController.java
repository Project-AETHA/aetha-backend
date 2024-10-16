package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.NotificationDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.NotificationService;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public boolean createNotification(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("Creating a notification");
            System.out.println("Creating a notification");
            //? Create a notification

            NotificationDTO notification = new NotificationDTO();
            notification.setType(NotifyType.PUSH_NOTIFICATION);
            notification.setSubject("New Notification");
            notification.setMessage("This is a new notification");
            notification.setSeen(false);
            notification.setLink("https://www.google.com");
            notification.setRecipient(userDetails.getUsername());

            notificationService.createNotification(notification);

//            logger.log(Level.SEVERE, "error log");
//            logger.log(Level.WARNING, "warning log");
//            logger.log(Level.INFO, "info log");
//            logger.log(Level.FINE, "debug log");
//            logger.log(Level.FINER, "trace log");

//            throw new Exception("Unimplemented Method");
            //?  On success returns true
            return true;
        } catch (Exception e) {
            log.error("Error creating a notification - {}", e.getMessage());
            System.out.println("Error creating a notification");
            return false;
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getMyNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userDetails));
    }

    @PatchMapping("/markAsRead/{notificationId}")
    public void markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }

}
