package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.service.UserService;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final AuthUserRepository userRepository;
    private final UserService userService;
    private final ResponseDTO responseDTO;


    @GetMapping("/home")
    public ResponseEntity<?> home() {

        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Hellow to home screen");
        responseDTO.setContent(null);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthUser user) {
        try {
            switch (userService.registerUser(user)) {
                case "06":
                    responseDTO.setCode(VarList.RSP_DUPLICATED);
                    responseDTO.setMessage("Username Already Exists");
                    responseDTO.setContent(null);
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

                case "00":
                    responseDTO.setCode(VarList.RSP_SUCCESS);
                    responseDTO.setMessage("User Created");
                    responseDTO.setContent(user);
                    return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

                default:
                    responseDTO.setCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Server Error");
                    responseDTO.setContent(null);
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_GATEWAY);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
