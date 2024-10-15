package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.NotificationDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "announcements")
    public void listenAnnouncements(NotificationDTO notificationDTO) {
        // Listen to announcements and send to all the subscribers
    }

}
