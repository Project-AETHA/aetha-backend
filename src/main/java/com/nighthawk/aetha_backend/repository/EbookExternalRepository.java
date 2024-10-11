package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EbookExternalRepository  extends MongoRepository<EbookExternal, String> {
    List<EbookExternal> findByAuthor(AuthUser authorId);
    EbookExternal findByTitle(String title);

    @Query("{'title': {$regex: ?0, $options: 'i'}, 'genres': {$all: ?1, $options: 'i'}, 'rating': {$gte: ?2}}")
    List<EbookExternal> searchEbooks(String searchTerm, List<String> genres, Double rating);
}
