package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ForumDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.ForumService;
import org.springframework.security.core.userdetails.UserDetails;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin
public class ForumController {

    private final ForumService forumService;
    private final ResponseDTO responseDTO;

    public ForumController(ForumService forumService, ResponseDTO responseDTO) {
        this.forumService = forumService;
        this.responseDTO = responseDTO;
    }

    @PostMapping("/createForum")
    public ResponseEntity<ResponseDTO> createForum(@RequestBody ForumDTO forumDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO response = new ResponseDTO();
        try {
            response = forumService.createForum(forumDTO, userDetails);
        } catch (Exception e) {
            response.setCode(VarList.RSP_ERROR);
            response.setMessage("Error saving forum");
            response.setContent(null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getForum")
    public ResponseEntity<ResponseDTO> getForum() {      
        return new ResponseEntity<>(forumService.getForum(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDTO> getForumById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO = forumService.getForumById(id);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error retrieving forum");
            responseDTO.setContent(null);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateForum(@PathVariable String id, @RequestBody ForumDTO forumDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO = forumService.updateForum(id, forumDTO);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error updating forum");
            responseDTO.setContent(null);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteForum(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO = forumService.deleteForum(id);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error deleting forum");
            responseDTO.setContent(null);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}