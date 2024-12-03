package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.FavPoem;
import com.nighthawk.aetha_backend.entity.Poem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavPoemRepository extends MongoRepository<FavPoem, String> {
    FavPoem findByPoemAndUser(Poem poem, AuthUser user);
}
