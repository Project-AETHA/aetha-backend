package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.RatingDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Rating;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.RatingRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Transactional
    public ResponseDTO saveRating(RatingDTO ratingDTO, UserDetails userDetails) {
        HashMap<String, String> errors = new HashMap<>();
       
        
        if (ratingDTO.getContent() == null || ratingDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Feedbacks cannot be empty.");
        }

        //check rating between 1 and 5
        if (ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5) {
            errors.put("rating", "Rating Error");
        }

        if (!errors.isEmpty()) {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Validation errors occurred.");
            responseDTO.setErrors(errors);
            responseDTO.setContent(null);
            return responseDTO;
        }

        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).get();
            Novel novel = novelRepository.findById(ratingDTO.getNovel()).get();

            // Rating rating = modelMapper.map(ratingDTO, Rating.class);
            Rating rating = new Rating();

            rating.setRating(ratingDTO.getRating());
            rating.setContent(ratingDTO.getContent());
            rating.setUser(user);
            rating.setNovel(novel);
            ratingRepository.save(rating);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Thank You for your Feedback");
            responseDTO.setContent(rating);
        }   catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User or Novel not found.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error saving the feedback: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO getRating() {
        
        try {
            List<Rating> rating = ratingRepository.findAll();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ratings retrieved successfully.");
            responseDTO.setContent(rating);
     }  catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching Rating");
            responseDTO.setContent(e.getMessage());
     }

        return responseDTO;

    }
        

    @Transactional
    public ResponseDTO getRatingById(String id) { 
        try {
            Optional<Rating> rating = ratingRepository.findById(id);
            if (rating.isPresent()) {
                RatingDTO ratingDTO = modelMapper.map(rating.get(), RatingDTO.class);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ratings found.");
                responseDTO.setContent(ratingDTO);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Ratings not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error finding rating: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteRating(String id) { 
        try {
            Optional<Rating> rating = ratingRepository.findById(id);

            if (rating.isPresent()) {
                ratingRepository.deleteById(id);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Rating deleted successfully.");
                responseDTO.setContent(null);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Rating not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error deleting Rating: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }
}
