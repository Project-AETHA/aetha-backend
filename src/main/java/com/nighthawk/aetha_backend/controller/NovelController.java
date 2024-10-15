package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/novels")
public class NovelController {

    private ResponseDTO response;

    @Autowired
    NovelService novelService;


    // TODO - moved on to finalizing the eBook module

    // ? Creating a novel
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNovel(@RequestBody Novel novel) {
        return ResponseEntity.ok(novelService.createNovel(novel));
    }

    // ? Update a novel
    // ! Should include a pre-check of the subscription count, if the content is updated
    @PatchMapping("/update/{novelId}")
    public ResponseEntity<ResponseDTO> updateNovel(@RequestBody Novel novel, @PathVariable String novelId) {
        return ResponseEntity.ok(novelService.updateNovel(novel));
    }

    // ? Deleting a novel
    // ! Should include a pre-check about any subscriptions before deleting a book
    @DeleteMapping("/delete/{novelId}")
    public ResponseEntity<ResponseDTO> deleteNovel(@PathVariable String novelId) {
        return ResponseEntity.ok(novelService.deleteNovel(novelId));
    }


    // ? Get all novels
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllNovels() {
        return ResponseEntity.ok(novelService.getAllNovels());
    }

    //? Get all novels paginated
    @GetMapping("/all/paginated")
    public ResponseEntity<ResponseDTO> getAllNovelsPaginated(
            @RequestBody RequestDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(novelService.getAllNovelsPaginated(page, pageSize));
    }

    // ? Search by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<ResponseDTO> getNovelsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(novelService.getNovelsByAuthor(authorId, page, pageSize));
    }

    // ? Search by title
    @PostMapping("/filter")
    public ResponseEntity<ResponseDTO> getNovelByTitle(
            @RequestBody RequestDTO requestDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int pageSize
    ) {
        return ResponseEntity.ok(novelService.filterNovels(requestDTO, page, pageSize));
    }

}
