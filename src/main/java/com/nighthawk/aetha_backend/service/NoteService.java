package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Note;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NotesRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private NotesRepository repository;

    @Autowired
    private AuthUserRepository userRepository;

    public Optional<List<Note>> getMyNotes(UserDetails userDetails) {

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user != null) {
            return repository.findNoteByOwner(user);
        }
        return Optional.empty();
    }

    public ResponseDTO createNote(UserDetails userDetails, Note note) {

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user != null) {
            try {
                note.setOwner(user);
                note.setCreatedAt(new Date());
                note.setLastModified(new Date());
                repository.save(note);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Note created successfully");
                responseDTO.setContent(note);
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Note creation failed: " + e.getMessage());
            }
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User not found");
        }

        return responseDTO;
    }

    public ResponseDTO getSingleNote(UserDetails userDetails, String noteId) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        Optional<Note> note = repository.findNoteById(noteId);

        if(note != null && note.isPresent()) {
            responseDTO.setContent(note);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Note found");
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Note not found");
        }

        return responseDTO;
    }

    public ResponseDTO updateNote(UserDetails userDetails, Note note) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user != null) {
            try {
                Note previous_note = repository.findNoteById(note.getId()).orElse(null);

                if(previous_note == null) {
                    responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                    responseDTO.setMessage("Note doesn't exist");
                } else {
                    note.setLastModified(new Date());
                    note.setCreatedAt(previous_note.getCreatedAt());
                    note.setOwner(user);
                    repository.save(note);
                    responseDTO.setCode(VarList.RSP_SUCCESS);
                    responseDTO.setMessage("Note updated successfully");
                    responseDTO.setContent(note);
                }

            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Note update failed : " + e.getMessage());
            }
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User not found");
        }

        return responseDTO;
    }
}
