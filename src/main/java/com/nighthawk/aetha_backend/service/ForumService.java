package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ForumDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Forum;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.ForumRepository;
import com.nighthawk.aetha_backend.entity.AuthUser;
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
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private AuthUserRepository userRepository;

    

    @Transactional
    public ResponseDTO createForum(ForumDTO forumDTO, UserDetails userDetails) {
        HashMap<String, String> errors = new HashMap<>();

        if (forumDTO.getTopic() == null || forumDTO.getTopic().trim().isEmpty()) {
            errors.put("topic", "Forum topic cannot be empty.");
        }

        if (forumDTO.getContent() == null || forumDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Forum content cannot be empty.");
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

            Forum forum = modelMapper.map(forumDTO, Forum.class);
            forum.setUser(user);
            forumRepository.save(forum);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Forum saved successfully.");
            responseDTO.setContent(forum);
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not logged in");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error saving the forum: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO getForum() {
        try{
        List<Forum> forums = forumRepository.findAll();
        
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Forums retrieved successfully.");
        responseDTO.setContent(forums);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching comment");
            responseDTO.setContent(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getForumById(String id) {
        Optional<Forum> forum = forumRepository.findById(id);

        if (forum.isPresent()) {
            ForumDTO forumDTO = modelMapper.map(forum.get(), ForumDTO.class);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Forum retrieved successfully.");
            responseDTO.setContent(forumDTO);
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Forum not found.");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateForum(String id, ForumDTO forumDTO) {
        HashMap<String, String> errors = new HashMap<>();

        if (forumDTO.getTopic() == null || forumDTO.getTopic().trim().isEmpty()) {
            errors.put("topic", "Forum topic cannot be empty.");
        }

        if (forumDTO.getContent() == null || forumDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Forum content cannot be empty.");
        }

        if (forumDTO.getUser() == null || forumDTO.getUser().trim().isEmpty()) {
            errors.put("author", "Author details are missing or invalid.");
        }

        if (!errors.isEmpty()) {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Validation errors occurred.");
            responseDTO.setErrors(errors);
            responseDTO.setContent(null);
            
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteForum(String id) {
        try {
            forumRepository.deleteById(id);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Forum deleted successfully.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error deleting the forum: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }
}

        

    