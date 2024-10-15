package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.PoemDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.service.PoemService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ResponseDTO> createPoem(
            @RequestBody PoemDTO poem,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        // ? methana function eka call kranawa withrai.e nisa parameter walata type eka denne na.
        return ResponseEntity.ok(poemService.createPoem(poem, userDetails));
    }

    @GetMapping("/get-all-poems")
    public ResponseEntity<ResponseDTO> getAllPoems(){

        return ResponseEntity.ok(poemService.getAllPoems());
    }

    @GetMapping("/find-poem/{id}")
    public ResponseEntity<ResponseDTO> findPoemById(@PathVariable String id) {

        return ResponseEntity.ok(poemService.findPoemById(id));
    }

    @GetMapping("delete-poem/{id}")
    public ResponseEntity<ResponseDTO> deletePoem(@PathVariable String id){

        return ResponseEntity.ok(poemService.deletePoem(id));

    }

    @PutMapping("update-poem/{id}")
    public ResponseEntity<ResponseDTO> updatePoem(
            @RequestBody Poem poem,
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        return ResponseEntity.ok(poemService.updatePoem(poem,id, userDetails));
    }

    @PutMapping("update/upvote/{id}")
    public ResponseEntity<ResponseDTO> updateUpvote(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(poemService.updateUpvote(id, userDetails));
    }

    @PutMapping("update/downvote/{id}")
    public ResponseEntity<ResponseDTO> updateDownvote(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(poemService.updateDownvote(id, userDetails));
    }





}
