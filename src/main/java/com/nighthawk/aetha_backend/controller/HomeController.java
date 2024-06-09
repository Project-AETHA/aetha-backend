package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {


    @Autowired
    private ResponseDTO responseDTO;

    @GetMapping
    public ResponseEntity<?> home() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("/api - Home Page");

        // Set the content like all the books here
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }

}
