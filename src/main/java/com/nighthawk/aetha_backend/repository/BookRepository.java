package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findBookByName(String name);
}
