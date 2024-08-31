package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.service.PoemService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ? samanyen backend eka run wens port eken ena request withrai accept karanne.e nisa one port ekakin ena ewa accept
// ? karanna puluwan widiyta annotation ekaka danna ona.
// ? restController annotation - recongnize as a Controller.
// ? RequestMapping - define the which requests accepts by this controller

@CrossOrigin // ? onema port ekakin ena request backend eken acces karanna me annotation pawichchi krnne
@RestController
@RequestMapping("/api/poems")
public class PoemController {

    @Autowired
    PoemService poemService;

    @Autowired
    ResponseDTO responseDTO;

    @PostMapping("/add-poem")
    public ResponseEntity<ResponseDTO> createPoem(@RequestBody Poem poem){

        // ? methana function eka call kranawa withrai.e nisa parameter walata type eka denne na.
        Poem savedPoem = poemService.createPoem(poem);

        if(savedPoem == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Poem creation failed");
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Poem created successfully");
            responseDTO.setContent(savedPoem);
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/get-all-poems")
    public ResponseEntity<ResponseDTO> getAllPoems(){

        return ResponseEntity.ok(poemService.getAllPoems());
    }

    @GetMapping("/find-poem/{id}")
    public ResponseEntity<ResponseDTO> findPoemById(@PathVariable String id) {

        return ResponseEntity.ok(poemService.findPoemById(id));
    }

}
