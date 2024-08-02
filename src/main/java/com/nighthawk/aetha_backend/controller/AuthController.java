package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.LoginRequest;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthUser user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
