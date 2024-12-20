package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.NovelDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/novels")
public class NovelController {

    @Autowired
    NovelService novelService;


    //? Get novel by Novel ID
    @GetMapping("/{novelId}")
    public ResponseEntity<ResponseDTO> getNovelById(@PathVariable String novelId) {
        return ResponseEntity.ok(novelService.getNovelById(novelId));
    }

    // ? Creating a novel
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNovel(
            @RequestBody NovelDTO novelDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "false") boolean isDraft
    ) {
        return ResponseEntity.ok(novelService.createNovel(novelDTO, userDetails, isDraft));
    }

    // ? Update a novel
    @PatchMapping("/update/{novelId}")
    public ResponseEntity<ResponseDTO> updateNovel(@RequestBody Novel novel, @PathVariable String novelId) {
        return ResponseEntity.ok(novelService.updateNovel(novel));
    }

    // ? Deleting a novel
    @DeleteMapping("/delete/{novelId}")
    public ResponseEntity<ResponseDTO> deleteNovel(@PathVariable String novelId) {
        return ResponseEntity.ok(novelService.deleteNovel(novelId));
    }


    // ? Get all novels based on status
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllPublishedNovels() {
        return ResponseEntity.ok(novelService.getAllPublishedNovels()
        );
    }

    // ? Get all novels based on status
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-novels")
    public ResponseEntity<ResponseDTO> getAllNovels() {
        return ResponseEntity.ok(novelService.getAllNovels()
        );
    }

    // ? Get all pending novels
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/pendingapprove")
    public ResponseEntity<ResponseDTO> getAllPendingNovels() {
        return ResponseEntity.ok(novelService.getAllPendingNovels()
        );
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

    //? Get my novels
    @GetMapping("/my")
    public ResponseEntity<ResponseDTO> getMyNovels(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(novelService.getMyNovels(userDetails));
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


    //? Get novel details including the chapters, reviews and user-info
    @GetMapping("/chapters-overview/{novelId}")
    public ResponseEntity<ResponseDTO> getNovelChaptersOverview(@PathVariable String novelId) {
        return ResponseEntity.ok(novelService.getNovelChaptersOverview(novelId));
    }

    //!!! Search results for novels
    // TODO - Implement the search functionality
    @PostMapping("/search")
    public ResponseEntity<ResponseDTO> searchNovels(
            @RequestBody RequestDTO requestDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
//        return ResponseEntity.ok(novelService.searchNovels(requestDTO, page, pageSize));
        return null;
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

    // ? view novel details to approve
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/viewToApprove/{novelId}")
    public ResponseEntity<ResponseDTO> viewToApproveNovel(@PathVariable String novelId){
        return  ResponseEntity.ok(novelService.viewToApproveNovel(novelId));
    }


    //? Approving a novel by the admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{novelId}")
    public ResponseEntity<ResponseDTO> approveNovel(@PathVariable String novelId) {
        return ResponseEntity.ok(novelService.approveNovel(novelId));
    }

    //? Rejecting a novel by the admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{novelId}")
    public ResponseEntity<ResponseDTO> rejectNovel(@PathVariable String novelId, @RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(novelService.rejectNovel(novelId, requestDTO));
    }

}
