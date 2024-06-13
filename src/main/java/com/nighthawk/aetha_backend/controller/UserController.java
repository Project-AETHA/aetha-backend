package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.LoginRequest;
import com.nighthawk.aetha_backend.dto.LoginResponse;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.service.UserService;
import com.nighthawk.aetha_backend.utils.Jwtutils;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ResponseDTO responseDTO;

    @Autowired
    private final Jwtutils jwtutils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        try {

            List<AuthUser> users = userService.getAllUsers();

            if(!users.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Listing all users");
                responseDTO.setContent(users);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("No users found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);

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

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Bad Credentials");
            responseDTO.setContent(null);

            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtutils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        try {
            Optional<AuthUser> user = userService.findUserByUsername(username);

            if(user.isPresent()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User found");
                responseDTO.setContent(user);

                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(user);

                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Server Error");
            responseDTO.setContent(null);

            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('AMDIN')")
    @PostMapping("/update/{username}")
    public ResponseEntity<?> updateUserRole(@PathVariable String username) {
        try {
            System.out.println("MESSAGE");
            return null;
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Server Error");
            responseDTO.setContent(null);

            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
    }

}
