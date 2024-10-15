package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Poem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// ? Entity,Primary key data type
// ? interface = abstract class , extends = inherit
// ? PoemRepository eka athule liyana okkoma abstract ewa.ewa api define kranawa withrai.athule logic eka liynne na.
// ? abstract class eka(PoemRepository) inherit kroth athule thiyena abstract ewa okkoma define kranna one(logic eka).

public interface PoemRepository extends MongoRepository<Poem, String>{

   // ? List<Poem> findPoemByAuthor(AuthUser author);
   long countByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
