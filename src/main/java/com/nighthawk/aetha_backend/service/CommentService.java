package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.repository.ChapterRepository;
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
    private ChapterRepository chapterRepository;

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
            Chapter chapter = chapterRepository.findById(commentDTO.getChapter()).get();

            Comment comment = modelMapper.map(commentDTO, Comment.class);
            comment.setUser(user);
            comment.setChapter(chapter);
            commentRepository.save(comment);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Comment saved successfully.");
            responseDTO.setContent(comment);
        }   catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User or Chapter of Novel not found.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error saving the comment: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO getCommentById(String chapterId, UserDetails userDetails) {
        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).get();
            Chapter chapter = chapterRepository.findById(chapterId).get();

            List<Comment> comments = commentRepository.findByChapterAndUser(chapter, user);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Your Personal Comments retrieved succesfully");
            responseDTO.setContent(comments);
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User or Chapter of Novel not found.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error finding comments: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO getComments(String novelId, Integer chapterNumber) {
        try {

            Optional<Chapter> chapters = chapterRepository.findByNovelIdAndChapterNumber(novelId, chapterNumber);

            //convert Optional to Chapter
            Chapter chapter = chapters.get();

            List<Comment> comments = commentRepository.findByChapter(chapter);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Comments retrieved succesfully");
            responseDTO.setContent(comments);
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User or Chapter of Novel not found.");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error finding comments: " + e.getMessage());
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
