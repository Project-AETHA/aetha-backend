package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.Subscription;
import com.nighthawk.aetha_backend.entity.SubscriptionTiers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    int countByUserAndNovel(AuthUser user, Novel novel);
    List<Subscription> findByUser(AuthUser user);

    SubscriptionTiers findByNovel(Novel novel);

    Optional<Subscription> findByUserAndNovel(AuthUser user, Novel novel);
}
