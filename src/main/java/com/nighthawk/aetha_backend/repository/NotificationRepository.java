package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
