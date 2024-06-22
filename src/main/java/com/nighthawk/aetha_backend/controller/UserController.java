package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.service.UserService;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ResponseDTO responseDTO;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<ResponseDTO> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        try {

            List<AuthUser> users = userService.getAllUsers();

            System.out.println(userService.findByEmail(userDetails.getUsername()));

            if(!users.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Listing all users");
                responseDTO.setContent(users);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("No users found");
                responseDTO.setContent(null);
            }
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<ResponseDTO> findByEmail(@PathVariable String email) {

        try {
            AuthUser user = userService.findByEmail(email);

            if(user != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User found");
                responseDTO.setContent(user);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Server Error");
            responseDTO.setContent(null);
        }

        return ResponseEntity.ok(responseDTO);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{email}")
    public ResponseEntity<ResponseDTO> updateUsers(@PathVariable String email, @RequestBody AuthUser user) {
        try {

            AuthUser savedUser = userService.updateUser(email, user);

            if(savedUser != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User found");
                responseDTO.setContent(savedUser);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User saving failed");
            responseDTO.setContent(null);
        }

        return ResponseEntity.ok(responseDTO);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable String email) {
        try {
            AuthUser deletedUser = userService.deleteUser(email);

            if(deletedUser != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User deleted successfully");
                responseDTO.setContent(deletedUser);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User deletion failed");
            responseDTO.setContent(null);
        }

        return ResponseEntity.ok(responseDTO);
    }

}
