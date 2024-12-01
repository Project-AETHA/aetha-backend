package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Rating;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByNovel(Novel novel);
    List<Rating> findByUser(AuthUser user);

    List<Rating> findByNovelAndUser(Novel novel, AuthUser user);
}



