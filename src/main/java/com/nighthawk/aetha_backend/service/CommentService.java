package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.CommentRepository;
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
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Transactional
    public ResponseDTO saveComment(CommentDTO commentDTO, UserDetails userDetails) {
        HashMap<String, String> errors = new HashMap<>();

       
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Comment content cannot be empty.");
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
            Novel novel = novelRepository.findById(commentDTO.getNovel()).get();

            Comment comment = modelMapper.map(commentDTO, Comment.class);
            comment.setUser(user);
            comment.setNovel(novel);
            commentRepository.save(comment);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Comment saved successfully.");
            responseDTO.setContent(comment);
        }   catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User or Novel not found.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error saving the comment: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO getComment() {
        
        try {
            List<Comment> comments = commentRepository.findAll();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Comments retrieved successfully.");
            responseDTO.setContent(comments);
     }  catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching comment");
            responseDTO.setContent(e.getMessage());
     }

        return responseDTO;

    }
        

    @Transactional
    public ResponseDTO getCommentById(String id) { 
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                CommentDTO commentDTO = modelMapper.map(comment.get(), CommentDTO.class);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Comment found.");
                responseDTO.setContent(commentDTO);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Comment not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error finding comment: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteComment(String id) { 
        try {
            Optional<Comment> comment = commentRepository.findById(id);

            if (comment.isPresent()) {
                commentRepository.deleteById(id);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Comment deleted successfully.");
                responseDTO.setContent(null);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Comment not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error deleting comment: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }
}
