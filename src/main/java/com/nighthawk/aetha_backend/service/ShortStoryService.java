package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.*;
import com.nighthawk.aetha_backend.entity.*;
import com.nighthawk.aetha_backend.repository.ShortStoryRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import com.nighthawk.aetha_backend.utils.predefined.ContentWarnings;
import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ShortStoryService {

    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    private ShortStoryRepository ShortStoryRepository;
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelMapper modelMapper;

    public ResponseDTO createShortStory(ShortStoryDTO shortStoryDTO, UserDetails userDetails) {
        ResponseDTO responseDTO = new ResponseDTO();
        HashMap<String, String> errors = new HashMap<>();
        ShortStory shortStory = new ShortStory();

        try {
            // Basic field validation
            validateBasicFields(shortStoryDTO, errors, shortStory);

            // Genre validation and conversion
            if (shortStoryDTO.getGenres() == null || shortStoryDTO.getGenres().isEmpty()) {
                errors.put("genres", "At least one genre must be selected");
            } else {
                List<Genres> genreList = new ArrayList<>();
                for (String genreString : shortStoryDTO.getGenres()) {
                    String trimmedGenre = genreString.trim().toUpperCase();
                    try {
                        Genres genre = Genres.valueOf(trimmedGenre);
                        genreList.add(genre);
                    } catch (IllegalArgumentException e) {
                        errors.put("genres", "Invalid genre: " + trimmedGenre);
                    }
                }
                shortStory.setGenres(genreList);
            }

            // Tags validation and conversion
            if (shortStoryDTO.getTags() != null && !shortStoryDTO.getTags().isEmpty()) {
                List<Tags2> tagsList = new ArrayList<>();
                for (String tagString : shortStoryDTO.getTags()) {
                    String trimmedTag = tagString.trim().toUpperCase();
                    try {
                        Tags2 tag = Tags2.valueOf(trimmedTag);
                        tagsList.add(tag);
                    } catch (IllegalArgumentException e) {
                        errors.put("tags", "Invalid tag: " + trimmedTag);
                    }
                }
                shortStory.setTags(tagsList);
            }

            // Custom tags handling
            if (shortStoryDTO.getCustomTags() != null) {
                shortStory.setCustomTags(shortStoryDTO.getCustomTags());
            }

            // Content warnings validation and conversion
            if (shortStoryDTO.getContentWarning() == null || shortStoryDTO.getContentWarning().isEmpty()) {
                errors.put("contentWarning", "Content warnings cannot be empty");
            } else {
                List<ContentWarnings> warningsList = new ArrayList<>();
                for (String warningString : shortStoryDTO.getContentWarning()) {
                    String trimmedWarning = warningString.trim().toUpperCase();
                    try {
                        ContentWarnings warning = ContentWarnings.valueOf(trimmedWarning);
                        warningsList.add(warning);
                    } catch (IllegalArgumentException e) {
                        errors.put("contentWarning", "Invalid warning: " + trimmedWarning);
                    }
                }
                shortStory.setContentWarning(warningsList);
            }

            // Handle manual release date
            if (shortStoryDTO.getManualReleaseDate() != null) {
                shortStory.setManualReleaseDate(shortStoryDTO.getManualReleaseDate());
            }

            // Set default values
            shortStory.setAuthor(authUserRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Author not found")));
            shortStory.setStatus(ContentStatus.PENDING);
            shortStory.setCreatedAt(LocalDate.now());
            shortStory.setRating(0.0f);
            shortStory.setViews(0);
            shortStory.setClicks(0);

            if (errors.isEmpty()) {
                ShortStory savedStory = ShortStoryRepository.save(shortStory);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Short story created successfully");
                responseDTO.setContent(savedStory);
            } else {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(shortStory);
                responseDTO.setErrors(errors);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred while creating short story");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    private void validateBasicFields(ShortStoryDTO shortStoryDTO, HashMap<String, String> errors, ShortStory shortStory) {
        if (shortStoryDTO.getTitle() == null || shortStoryDTO.getTitle().trim().isEmpty()) {
            errors.put("title", "Title cannot be empty");
        } else {
            shortStory.setTitle(shortStoryDTO.getTitle().trim());
        }

        if (shortStoryDTO.getSynopsis() == null || shortStoryDTO.getSynopsis().trim().isEmpty()) {
            errors.put("synopsis", "Synopsis cannot be empty");
        } else {
            shortStory.setSynopsis(shortStoryDTO.getSynopsis().trim());
        }

        if (shortStoryDTO.getDescription() == null || shortStoryDTO.getDescription().trim().isEmpty()) {
            errors.put("description", "Description cannot be empty");
        } else {
            shortStory.setDescription(shortStoryDTO.getDescription().trim());
        }

        if (shortStoryDTO.getCoverImage() == null || shortStoryDTO.getCoverImage().trim().isEmpty()) {
            errors.put("coverImage", "Cover image cannot be empty");
        } else {
            shortStory.setCoverImage(shortStoryDTO.getCoverImage().trim());
        }
    }

    public ResponseDTO getAllShortStorys() {

        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All ShortStorys fetched successfully");
            responseDTO.setContent(ShortStoryRepository.findAll());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching ShortStorys");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    //? Paginated response
    public ResponseDTO getAllShortStorysPaginated(int page, int pageSize) {

        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<ShortStory> ShortStorysPage = ShortStoryRepository.findAll(pageable);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All ShortStorys fetched successfully");
            responseDTO.setContent(ShortStorysPage);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching ShortStorys");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    public ResponseDTO getShortStorysByAuthor(String authorId, int page, int pageSize) {

        try {
            //? Getting the author data
            AuthUser author = authUserRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));

            Pageable pageable = PageRequest.of(page, pageSize);
            Page<ShortStory> ShortStorysByArtists = ShortStoryRepository.findByAuthor(author, pageable);

            responseDTO.setContent(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStorys relevant to the artist fetched successfully");
            responseDTO.setContent(ShortStorysByArtists);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO filterShortStorys(RequestDTO requestDTO, int page, int pageSize) {

        //? Search logic for searching ShortStorys by title
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
            List<ShortStory> ShortStorys = mongoTemplate.find(query, ShortStory.class);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStorys relevant to the title fetched successfully");

            //! Set the content
            responseDTO.setContent(ShortStorys);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO updateShortStory(ShortStory ShortStory) {

        //! Should include a pre-check of the subscription count, if the content is updated
        //? Update logic

        return responseDTO;
    }

    public ResponseDTO deleteShortStory(String ShortStoryId) {

        //! Should include a pre-check about any subscriptions before deleting a book
        //? Deletion logic

        return responseDTO;
    }

    public ResponseDTO getShortStoryById(String ShortStoryId) {
        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStory fetched successfully");
            responseDTO.setContent(ShortStoryRepository.findById(ShortStoryId).orElseThrow(() -> new RuntimeException("ShortStory not found")));
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching ShortStory");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO approveShortStory(String ShortStoryId) {
        try {
            ShortStory ShortStory = ShortStoryRepository.findById(ShortStoryId).orElseThrow(() -> new RuntimeException("ShortStory not found"));
            ShortStory.setStatus(ContentStatus.APPROVED);
            ShortStoryRepository.save(ShortStory);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStory approved successfully");
            responseDTO.setContent(ShortStory);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error approving ShortStory");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO rejectShortStory(String ShortStoryId) {
        try {

            //? Updating the ShortStory's status
            ShortStory ShortStory = ShortStoryRepository.findById(ShortStoryId).orElseThrow(() -> new RuntimeException("ShortStory not found"));
            ShortStory.setStatus(ContentStatus.REJECTED);
            ShortStoryRepository.save(ShortStory);

            //? Sending notification to the author about the rejection
            NotificationDTO notification = new NotificationDTO();
            notification.setType(NotifyType.PUSH_NOTIFICATION);
            notification.setCategory(NotificationCategory.UPDATE);
            notification.setSubject("ShortStory Rejected");
            notification.setMessage("The ShortStory " + ShortStory.getTitle() + " has been rejected");
            notification.setLink("/author/ShortStorys/" + ShortStory.getId());
            notification.setRecipient(ShortStory.getAuthor());


            if(!notificationService.createNotification(notification)) {
                throw new RuntimeException("Error sending notification");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStory declined successfully");
            responseDTO.setContent(ShortStory);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error rejecting ShortStory");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getMyShortStorys(UserDetails userDetails) {
        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            List<ShortStory> ShortStorys = ShortStoryRepository.findByAuthor(user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("ShortStorys fetched successfully");
            responseDTO.setContent(ShortStorys);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching ShortStorys");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

}
