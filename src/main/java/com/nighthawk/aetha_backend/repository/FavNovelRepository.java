package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.FavNovel;
import com.nighthawk.aetha_backend.entity.Novel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavNovelRepository extends MongoRepository<FavNovel, String> {
    Optional<FavNovel> findByNovelAndUser(Novel novel, AuthUser user);

    List<FavNovel> findByUser(AuthUser user);
}
