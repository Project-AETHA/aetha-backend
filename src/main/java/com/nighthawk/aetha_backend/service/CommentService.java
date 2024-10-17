package com.nighthawk.aetha_backend.service;

import org.springframework.stereotype.Service;
import com.nighthawk.aetha_backend.dto.CommentDTO;
import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import java.util.List;
import org.modelmapper.TypeToken;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CommentDTO saveComment(CommentDTO commentDTO) {
        commentRepository.save(modelMapper.map(commentDTO, Comment.class));
        return commentDTO;
    }

    public List<CommentDTO> getAllComments() {
       List<Comment>commentList = commentRepository.findAll();
         return modelMapper.map(commentList,new TypeToken<List<CommentDTO>>(){}.getType());
    }

}
