package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.HashMap;

@Service
public class NovelService {

    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private View error;
    @Autowired
    private AuthUserRepository authUserRepository;

    public ResponseDTO createNovel(Novel novel) {

        HashMap<String, String> errors = new HashMap<>();

        try {
            //? Validation logic
            if(novel.getTitle().isEmpty()) errors.put("title", "Title cannot be empty");
            if(novel.getSynopsis().isEmpty()) errors.put("synopsis", "Synopsis cannot be empty");
            if(novel.getDescription().isEmpty()) errors.put("description", "Description cannot be empty");
            if(novel.getCoverImage().isEmpty()) errors.put("coverImage", "Cover image cannot be empty");
            if(novel.getGenre().isEmpty()) errors.put("genre", "Genre cannot be empty");
            if(novel.getContentWarning().isEmpty()) errors.put("contentWarning", "Content warnings cannot be empty");

            if(errors.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Novel created successfully");
                responseDTO.setContent(novelRepository.save(novel));
            } else {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(novel);
                responseDTO.setErrors(errors);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error Occured");
            responseDTO.setContent(e.getMessage());
        }

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


    public ResponseDTO getNovelsByAuthor(String authorId, int page, int pageSize) {

        // TODO - Test this endpoint ( must change the data type of the author attribute to ObjectID )

        //? Search logic for searching novels by an author
        try {

            //? Getting the author data
            AuthUser author = authUserRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));

            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Novel> novelsByArtists = novelRepository.findByAuthor(author, pageable);

            responseDTO.setContent(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novels relevant to the artist fetched successfully");
            responseDTO.setContent(novelsByArtists);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

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
