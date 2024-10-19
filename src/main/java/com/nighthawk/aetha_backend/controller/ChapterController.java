package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    ChapterService chapterService;

    //? Get all chapters related to a novel
    @GetMapping("/all/{novelId}")
    public ResponseEntity<ResponseDTO> getAllChaptersByNovelId(@PathVariable String novelId) {
        return ResponseEntity.ok(chapterService.getAllChaptersByNovelIdAndStatusAndVisibility(novelId));
    }

    //? Get all chapters related to a novel
    @PreAuthorize("hasRole('WRITER')")
    @GetMapping("/all/author/{novelId}")
    public ResponseEntity<ResponseDTO> getAllChaptersByNovelId(
            @PathVariable String novelId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(chapterService.getAllChaptersByNovelId(novelId, userDetails));
    }

    //? Getting the chapter based on the chapter id
    @GetMapping("/single/{chapterId}")
    public ResponseEntity<ResponseDTO> getChapterById(@PathVariable String chapterId) {
        return ResponseEntity.ok(chapterService.getChapterById(chapterId));
    }

    //? Creating a new chapter
    //! Should be able to save the completed chapter or the draft chapter
    //* Whether the novel is complete or not should be state as a parameter
    @PreAuthorize("hasRole('WRITER')")
    @PostMapping("/create/{novelId}")
    public ResponseEntity<ResponseDTO> createChapter(
            @PathVariable String novelId,
            @RequestParam(defaultValue = "false") Boolean isComplete,
            @RequestBody Chapter chapter,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(chapterService.createChapter(novelId, isComplete, chapter, userDetails));
    }

    //? Updating a chapter's visibility
    @PreAuthorize("hasRole('WRITER')")
    @PatchMapping("/update/visibility/{chapterId}")
    public ResponseEntity<ResponseDTO> updateChapterVisibility(
            @PathVariable String chapterId,
            @RequestParam Boolean isVisible,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(chapterService.updateChapterVisibility(chapterId, isVisible, userDetails));
    }

    //? Updating a chapter's content
    //! Auto saving should be handle through here or with a separate function
    @PreAuthorize("hasRole('WRITER')")
    @PutMapping("/update/{chapterId}")
    public ResponseEntity<ResponseDTO> updateChapter(
            @PathVariable String chapterId,
            @RequestBody Chapter chapter,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(chapterService.updateChapter(chapterId, chapter, userDetails));
    }


}
