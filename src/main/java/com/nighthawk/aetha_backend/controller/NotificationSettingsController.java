package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.entity.NotificationSettings;
import com.nighthawk.aetha_backend.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationSettingsController {

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @PostMapping("/settings")
    public ResponseEntity<String> saveNotificationSettings(@RequestBody NotificationSettings settings) {
        notificationSettingsService.saveNotificationSettings(settings);
        return ResponseEntity.ok("Notification settings saved successfully");
    }

    @GetMapping("/settings/{userId}")
    public ResponseEntity<NotificationSettings> getNotificationSettings(@PathVariable String userId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(userId);
        return settings != null ? ResponseEntity.ok(settings) : ResponseEntity.notFound().build();
    }
}
