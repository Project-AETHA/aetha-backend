package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.dto.ForumDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.CommentService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final ResponseDTO responseDTO;

    public CommentController(CommentService commentService, ResponseDTO responseDTO) {
        this.commentService = commentService;
        this.responseDTO = responseDTO;
    }

    @GetMapping("/getComment")
    public ResponseEntity<ResponseDTO> getComment() {
        return new ResponseEntity<>(commentService.getComment(), HttpStatus.OK);
    }
  

    @PostMapping("/saveComment")
    public ResponseEntity<ResponseDTO> saveComment(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO response = new ResponseDTO(); 
        try {
            response = commentService.saveComment(commentDTO, userDetails); 
        } catch (Exception e) {
            response.setCode(VarList.RSP_ERROR);
            response.setMessage("Error saving comment");
            response.setContent(null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
