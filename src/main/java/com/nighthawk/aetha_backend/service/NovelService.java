package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NovelService {

    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    private NovelRepository novelRepository;

    public ResponseDTO createNovel(Novel novel) {

        //? Creation logic
        novelRepository.save(novel);

        return responseDTO;
    }

    public ResponseDTO getAllNovels() {

        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All novels fetched successfully");
            responseDTO.setContent(novelRepository.findAll());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching novels");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    //? Paginated response
    public ResponseDTO getAllNovelsPaginated(int page, int pageSize) {

        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Novel> novelsPage = novelRepository.findAll(pageable);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All novels fetched successfully");
            responseDTO.setContent(novelsPage);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching novels");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    public ResponseDTO getNovelsByAuthor(String authorId) {

        //? Search logic for searching novels by an author

        return responseDTO;
    }

    public ResponseDTO getNovelByTitle(String title) {

        //? Search logic for searching novels by title

        return responseDTO;
    }

    public ResponseDTO getNovelsByTags(RequestDTO request) {

        //? Search logic for searching novels by tags

        return responseDTO;
    }

    public ResponseDTO getNovelsByGenres(RequestDTO request) {

        //? Search logic for searching novels by genres

        return responseDTO;
    }

    public ResponseDTO updateNovel(Novel novel) {

        //! Should include a pre-check of the subscription count, if the content is updated
        //? Update logic

        return responseDTO;
    }

    public ResponseDTO deleteNovel(String novelId) {

        //! Should include a pre-check about any subscriptions before deleting a book
        //? Deletion logic

        return responseDTO;
    }
}
