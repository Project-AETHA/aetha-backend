package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Blog;
import com.nighthawk.aetha_backend.service.BlogService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api")
public class HomeController {


    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private BlogService blogService;

    //? Route open for all users including guest users
    @GetMapping
    public ResponseEntity<?> home() {

        List<Blog> blogs = blogService.getAllBlogs();

        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Landing Page");
        responseDTO.setContent(blogs);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }

    //? For testing the Role based authentication - For Reader
    @PreAuthorize("hasRole('READER')")
    @GetMapping("/reader")
    public ResponseEntity<?> userReader() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Welcome, Writer");

        // Set the content like all the books here
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }

    //? For testing the Role based authentication - For Writer
    @PreAuthorize("hasRole('WRITER')")
    @GetMapping("/writer")
    public ResponseEntity<?> userWriter() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Welcome, Writer");
        responseDTO.setContent(null);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }

}
