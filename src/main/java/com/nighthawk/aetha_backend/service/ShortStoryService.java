package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.NotificationDTO;
import com.nighthawk.aetha_backend.dto.ShortStoryDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.ShortStory;
import com.nighthawk.aetha_backend.entity.Tags;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.ShortStoryRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import com.nighthawk.aetha_backend.utils.predefined.ContentWarnings;
import com.nighthawk.aetha_backend.utils.predefined.NotificationCategory;
import com.nighthawk.aetha_backend.utils.predefined.NotifyType;
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

    public ResponseDTO createShortStory(ShortStoryDTO ShortStoryDTO, UserDetails userDetails) {

        HashMap<String, String> errors = new HashMap<>();
        ShortStory ShortStory = new ShortStory();

        try {
            //? Validation logic
            if(ShortStoryDTO.getTitle() == null || ShortStoryDTO.getTitle().isEmpty()) errors.put("title", "Title cannot be empty");
            else ShortStory.setTitle(ShortStoryDTO.getTitle());

            if(ShortStoryDTO.getSynopsis() == null || ShortStoryDTO.getSynopsis().isEmpty()) errors.put("synopsis", "Synopsis cannot be empty");
            else ShortStory.setSynopsis(ShortStoryDTO.getSynopsis());

            if(ShortStoryDTO.getDescription() == null || ShortStoryDTO.getDescription().isEmpty()) errors.put("description", "Description cannot be empty");
            else ShortStory.setDescription(ShortStoryDTO.getDescription());

            if(ShortStoryDTO.getCoverImage() == null || ShortStoryDTO.getCoverImage().isEmpty()) errors.put("coverImage", "Cover image cannot be empty");
            else ShortStory.setCoverImage(ShortStoryDTO.getCoverImage());

            if(ShortStoryDTO.getGenres() == null) errors.put("genre", "Genre cannot be empty");
            else {
                List<Genres> genreList = new ArrayList<>();
                for (String genreString : ShortStoryDTO.getGenres()) {
                    String trimmedGenre = genreString.trim().toUpperCase();
                    try {
                        Genres genre = Genres.valueOf(trimmedGenre);
                        genreList.add(genre);
                    } catch (IllegalArgumentException e) {
                        errors.put("genres", "Invalid genre: " + trimmedGenre);
                    }
                }
                ShortStory.setGenres(genreList);
            }

            if(ShortStoryDTO.getTags() != null) {
                List<Tags> tagsList = new ArrayList<>();
                for (String tagString : ShortStoryDTO.getTags()) {
                    String trimmedTag = tagString.trim().toUpperCase();
                    try {
                        Tags tag = Tags.valueOf(trimmedTag);
                        tagsList.add(tag);
                    } catch (IllegalArgumentException e) {
                        errors.put("tags", "Invalid tag: " + trimmedTag);
                    }
                }
                ShortStory.setTags(tagsList);
            }

            if(ShortStoryDTO.getContentWarning() == null) errors.put("contentWarning", "Content warnings cannot be empty");
            else {
                List<ContentWarnings> warningsList = new ArrayList<>();
                for (String warningString : ShortStoryDTO.getContentWarning()) {
                    String trimmedWarning = warningString.trim().toUpperCase();
                    try {
                        ContentWarnings warning = ContentWarnings.valueOf(trimmedWarning);
                        warningsList.add(warning);
                    } catch (IllegalArgumentException e) {
                        errors.put("contentWarning", "Invalid warning: " + trimmedWarning);
                    }
                }
                ShortStory.setContentWarning(warningsList);
            }

            ShortStory.setAuthor(authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Author not found")));
            ShortStoryDTO.setStatus(ContentStatus.PENDING);

            //? TODO - Manual release date

            if(errors.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("ShortStory created successfully");
                responseDTO.setContent(ShortStoryRepository.save(ShortStory));
            } else {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(ShortStory);
                responseDTO.setErrors(errors);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error Occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
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
