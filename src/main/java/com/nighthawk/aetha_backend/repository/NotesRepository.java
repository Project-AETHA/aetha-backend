package com.nighthawk.aetha_backend.repository;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends MongoRepository<Note, String> {
    Optional<List<Note>> findNoteByOwner(AuthUser owner);

    Optional<Note> findNoteById(String noteId);
}
