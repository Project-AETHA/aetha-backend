package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Tags;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private ResponseDTO responseDTO;

    @GetMapping("/genres")
    public ResponseEntity<ResponseDTO> getGenres () {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Genres fetched successfully");
        responseDTO.setContent(Genres.values());

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("tags")
    public ResponseEntity<ResponseDTO> getTags () {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Genres fetched successfully");
        responseDTO.setContent(Tags.values());

        return ResponseEntity.ok(responseDTO);
    }

}
