package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.FollowUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FollowUserRepository extends MongoRepository<FollowUser, String> {
    Optional<FollowUser> findByFollowerAndFollowing(AuthUser follower, AuthUser following);
    List<FollowUser> findByFollower(AuthUser user);
    List<FollowUser> findByFollowing(AuthUser user);

    long countByFollowing(AuthUser user);

    long countByFollower(AuthUser user);
}
