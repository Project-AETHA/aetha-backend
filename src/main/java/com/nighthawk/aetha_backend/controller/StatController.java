package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.StatService;
import com.nighthawk.aetha_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/stats")
public class StatController {

    @Autowired
    private StatService statService;



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-dashboard")
    public ResponseEntity<ResponseDTO> getStatistics (){
        return ResponseEntity.ok(statService.getStatistics());
    }
}
