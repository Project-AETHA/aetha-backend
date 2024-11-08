package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.NotificationSettings;
import com.nighthawk.aetha_backend.repository.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationSettingsService {

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    public void saveNotificationSettings(NotificationSettings settings) {
        notificationSettingsRepository.save(settings); // Save settings to MongoDB
    }

    public NotificationSettings getNotificationSettings(String userId) {
        return notificationSettingsRepository.findByUserId(userId); // Retrieve settings by user ID
    }
}
