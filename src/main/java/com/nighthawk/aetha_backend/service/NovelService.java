package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.*;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.Tags;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.ChapterRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import com.nighthawk.aetha_backend.utils.predefined.ContentWarnings;
import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NovelService {

    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ResponseDTO createNovel(NovelDTO novelDTO, UserDetails userDetails) {

        HashMap<String, String> errors = new HashMap<>();
        Novel novel = new Novel();

        try {
            //? Validation logic
            if(novelDTO.getTitle() == null || novelDTO.getTitle().isEmpty()) errors.put("title", "Title cannot be empty");
            else novel.setTitle(novelDTO.getTitle());

            if(novelDTO.getSynopsis() == null || novelDTO.getSynopsis().isEmpty()) errors.put("synopsis", "Synopsis cannot be empty");
            else novel.setSynopsis(novelDTO.getSynopsis());

            if(novelDTO.getDescription() == null || novelDTO.getDescription().isEmpty()) errors.put("description", "Description cannot be empty");
            else novel.setDescription(novelDTO.getDescription());

            if(novelDTO.getCoverImage() == null || novelDTO.getCoverImage().isEmpty()) errors.put("coverImage", "Cover image cannot be empty");
            else novel.setCoverImage(novelDTO.getCoverImage());

            if(novelDTO.getGenres() == null) errors.put("genre", "Genre cannot be empty");
            else {
                List<Genres> genreList = new ArrayList<>();
                for (String genreString : novelDTO.getGenres()) {
                    String trimmedGenre = genreString.trim().toUpperCase();
                    try {
                        Genres genre = Genres.valueOf(trimmedGenre);
                        genreList.add(genre);
                    } catch (IllegalArgumentException e) {
                        errors.put("genres", "Invalid genre: " + trimmedGenre);
                    }
                }
                novel.setGenres(genreList);
            }

            if(novelDTO.getTags() != null) {
                List<Tags> tagsList = new ArrayList<>();
                for (String tagString : novelDTO.getTags()) {
                    String trimmedTag = tagString.trim().toUpperCase();
                    try {
                        Tags tag = Tags.valueOf(trimmedTag);
                        tagsList.add(tag);
                    } catch (IllegalArgumentException e) {
                        errors.put("tags", "Invalid tag: " + trimmedTag);
                    }
                }
                novel.setTags(tagsList);
            }

            if(novelDTO.getContentWarning() == null) errors.put("contentWarning", "Content warnings cannot be empty");
            else {
                List<ContentWarnings> warningsList = new ArrayList<>();
                for (String warningString : novelDTO.getContentWarning()) {
                    String trimmedWarning = warningString.trim().toUpperCase();
                    try {
                        ContentWarnings warning = ContentWarnings.valueOf(trimmedWarning);
                        warningsList.add(warning);
                    } catch (IllegalArgumentException e) {
                        errors.put("contentWarning", "Invalid warning: " + trimmedWarning);
                    }
                }
                novel.setContentWarning(warningsList);
            }

            novel.setAuthor(authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Author not found")));
            novelDTO.setStatus(ContentStatus.PENDING);

            //? TODO - Manual release date

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
            responseDTO.setMessage("Error Occurred");
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

    public ResponseDTO filterNovels(RequestDTO requestDTO, int page, int pageSize) {

        //? Search logic for searching novels by title
        try {
            //* Creating the pagination object
            Pageable pageable = PageRequest.of(page, pageSize);

            //* Creating the query object
            Query query = new Query();

            //* Adding the required criteria based on the given request
            if(!requestDTO.getTitle().isEmpty()) {
                query.addCriteria(Criteria.where("title").regex(".*" + requestDTO.getTitle() + ".*", "i"));
            }

            //* Include the pageable object to perform pagination on the resultant query
            query.with(pageable);

            //* Run the query and get the results
            List<Novel> novels = mongoTemplate.find(query, Novel.class);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novels relevant to the title fetched successfully");

            //! Set the content
            responseDTO.setContent(novels);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

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

    public ResponseDTO getNovelById(String novelId) {
        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novel fetched successfully");
            responseDTO.setContent(novelRepository.findById(novelId).orElseThrow(() -> new RuntimeException("Novel not found")));
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching novel");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO approveNovel(String novelId) {
        try {
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new RuntimeException("Novel not found"));
            novel.setStatus(ContentStatus.APPROVED);
            novelRepository.save(novel);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novel approved successfully");
            responseDTO.setContent(novel);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error approving novel");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO rejectNovel(String novelId) {
        try {

            //? Updating the novel's status
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new RuntimeException("Novel not found"));
            novel.setStatus(ContentStatus.REJECTED);
            novelRepository.save(novel);

            //? Sending notification to the author about the rejection
            NotificationDTO notification = new NotificationDTO();
            notification.setType(NotifyType.PUSH_NOTIFICATION);
            notification.setCategory(NotificationCategory.UPDATE);
            notification.setSubject("Novel Rejected");
            notification.setMessage("The novel " + novel.getTitle() + " has been rejected");
            notification.setLink("/author/novels/" + novel.getId());
            notification.setRecipient(novel.getAuthor());


            if(!notificationService.createNotification(notification)) {
                throw new RuntimeException("Error sending notification");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novel declined successfully");
            responseDTO.setContent(novel);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error rejecting novel");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getMyNovels(UserDetails userDetails) {
        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            List<Novel> novels = novelRepository.findByAuthor(user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Novels fetched successfully");
            responseDTO.setContent(novels);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching novels");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getNovelChaptersOverview(String novelId) {

        try {
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new RuntimeException("Novel not found"));

            // TODO - Implement the logic to get the chapters and reviews
            List<ChapterDTO> chapters = modelMapper.map(
                    chapterRepository.findAllByNovelAndStatusAndIsVisible(novel, "COMPLETED", true),
                    new TypeToken<List<ChapterDTO>>(){}.getType() //* To a list of ChapterDTO
            );

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data found");

            //? A DTO that contains the novel, chapters and reviews
            NovelChapterOverview novelChapterOverview = new NovelChapterOverview();
            novelChapterOverview.setNovel(novel);
            novelChapterOverview.setChapters(chapters);
            novelChapterOverview.setReviews(null);
            // TODO - Add the reviews later

            responseDTO.setContent(novelChapterOverview);

        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Error - Novel not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
