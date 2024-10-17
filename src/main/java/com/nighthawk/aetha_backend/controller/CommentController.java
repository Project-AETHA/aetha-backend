package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getComment")
    public List<CommentDTO> getComment() {
        return commentService.getAllComments();
    }

    @PostMapping("/saveComment")
    public CommentDTO saveComment(@RequestBody CommentDTO commentDTO) {
        return commentService.saveComment(commentDTO);
    }

}
