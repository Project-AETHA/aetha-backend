package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByEmail(String email);

    @Query("{ 'createdAt': { $gte: ?0, $lt: ?1 }, 'role': ?2 }")
    int countByCreatedAtBetweenAndRole(LocalDate startDate, LocalDate endDate, String role);
}
