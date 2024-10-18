package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    ChapterService chapterService;

    //? Get all chapters related to a novel
    @GetMapping("/all/{novelId}")
    public ResponseEntity<ResponseDTO> getAllChaptersByNovelId(String novelId) {
        return ResponseEntity.ok(chapterService.getAllChaptersByNovelId(novelId));
    }

    //? Getting the chapter based on the chapter id
    @GetMapping("/single/{chapterId}")
    public ResponseEntity<ResponseDTO> getChapterById(String chapterId) {
        return ResponseEntity.ok(chapterService.getChapterById(chapterId));
    }

    //? Creating a new chapter
    //! Should be able to save the completed chapter or the draft chapter

    //? Updating a chapter's visibility


    //? Updating a chapter's content
    //! Auto saving should be handle through here or with a separate function


}
