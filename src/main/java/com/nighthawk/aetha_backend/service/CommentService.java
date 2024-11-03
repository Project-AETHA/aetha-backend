package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.repository.CommentRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Transactional
    public ResponseDTO saveComment(CommentDTO commentDTO) {
        HashMap<String, String> errors = new HashMap<>();

       
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            errors.put("content", "Comment content cannot be empty.");
        }

      
        if (commentDTO.getUserId() == null || commentDTO.getUserId().trim().isEmpty()) {
            errors.put("author", "Author details are missing or invalid.");
        }

        if (!errors.isEmpty()) {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Validation errors occurred.");
            responseDTO.setErrors(errors);
            responseDTO.setContent(null);
            return responseDTO;
        }

        try {
            Comment comment = modelMapper.map(commentDTO, Comment.class);
            commentRepository.save(comment);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Comment saved successfully.");
            responseDTO.setContent(commentDTO);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error saving the comment: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO getAllComments() {
        try {
            List<Comment> commentList = commentRepository.findAll();
            List<CommentDTO> commentDTOList = modelMapper.map(commentList, new TypeToken<List<CommentDTO>>() {}.getType());

            if (commentDTOList.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No comments found.");
                responseDTO.setContent(null);
            } else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Comments retrieved successfully.");
                responseDTO.setContent(commentDTOList);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error retrieving comments: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

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
