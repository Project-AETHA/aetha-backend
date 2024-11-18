package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Settings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingsRepository extends MongoRepository<Settings,Integer> {

}
