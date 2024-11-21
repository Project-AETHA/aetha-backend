package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.NotificationSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingsRepository extends MongoRepository<NotificationSettings, String> {

    NotificationSettings findByUserId(String userId); // Custom method to find by user ID
}
