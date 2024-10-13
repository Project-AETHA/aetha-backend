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

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<ResponseDTO> findByEmail(@PathVariable String email){

        return ResponseEntity.ok(userService.findByEmail(email));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{email}")
    public ResponseEntity<ResponseDTO> updateUsers(@PathVariable String email, @RequestBody AuthUser user) {

        return ResponseEntity.ok(userService.updateUser(email,user));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDTO> deleteUser   (@PathVariable String email) {


        return ResponseEntity.ok(userService.deleteUser(email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-user")
    public ResponseEntity<ResponseDTO> addUser (@RequestBody AuthUser user){

        return ResponseEntity.ok(userService.addUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disable/{email}")
    public ResponseEntity<ResponseDTO> disableUser (@PathVariable String email){

        return ResponseEntity.ok(userService.disableUser(email));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stat")
    public ResponseEntity<ResponseDTO> getStatistics (){

        return ResponseEntity.ok(userService.getStatistics());
    }
}
