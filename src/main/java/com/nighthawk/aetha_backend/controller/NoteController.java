package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Blog;
import com.nighthawk.aetha_backend.entity.Note;
import com.nighthawk.aetha_backend.service.BlogService;
import com.nighthawk.aetha_backend.service.NoteService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private NoteService noteService;

    //? Route open for all users including guest users
    //? For testing the Role based authentication - For Reader

    @PreAuthorize("hasRole('WRITER')")
    @GetMapping("/get-my-notes")
    public ResponseEntity<ResponseDTO> getNotesById(@AuthenticationPrincipal UserDetails userDetails) {

        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All my notes");
            responseDTO.setContent(noteService.getMyNotes(userDetails));
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: " + e.getMessage());
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('WRITER')")
    @GetMapping("/get-single-note/{noteId}")
    public ResponseEntity<ResponseDTO> getSingleNoteById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String noteId) {
        return new ResponseEntity<>(noteService.getSingleNote(userDetails,  noteId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping("/create-note")
    public ResponseEntity<ResponseDTO> createNote(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody Note note
    ) {
        return new ResponseEntity<>(noteService.createNote(userDetails, note), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping("/update-note")
    public ResponseEntity<ResponseDTO> updateNote(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Note note
    ) {
        return new ResponseEntity<>(noteService.updateNote(userDetails, note), HttpStatus.OK);
    }

}
