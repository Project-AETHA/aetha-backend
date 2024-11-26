package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.SubscriptionTiers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionTiersRepository extends MongoRepository<SubscriptionTiers, String> {
    int countByNovel(Novel novel);

    Optional<SubscriptionTiers> findByNovel(Novel novel);

    Optional<SubscriptionTiers> findByNovelId(String novelId);
}
