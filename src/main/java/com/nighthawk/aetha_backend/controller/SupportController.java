package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.SupportType;
import com.nighthawk.aetha_backend.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/create_ticket")
    public ResponseEntity<ResponseDTO> createTicket(
            @RequestPart("title") String title,
            @RequestPart("category") String category,
            @RequestPart("description") String description,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        System.out.println(category);
        return ResponseEntity.ok(supportService.createTicket(title, category, description, files, userDetails));
    }

    @PostMapping("/update_ticket/{id}")
    public ResponseEntity<ResponseDTO> updateTicket(
            @RequestPart("title") String title,
            @RequestPart("category") String category,
            @RequestPart("description") String description,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ){
        return ResponseEntity.ok(supportService.updateTicket(title, category, description, files, userDetails, id));
    }

    @GetMapping("/get_all_tickets")
    public ResponseEntity<ResponseDTO> getAllTickets() {
        return ResponseEntity.ok(supportService.getAllTickets());
    }

    @GetMapping("/get_my_tickets")
    public ResponseEntity<ResponseDTO> getMyTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(supportService.getTicketByEmail(userDetails));
    }

    @GetMapping("/get_ticket/{id}")
    public ResponseEntity<ResponseDTO> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(supportService.getTicketById(id));
    }

    @DeleteMapping("/delete_ticket/{id}")
    public ResponseEntity<ResponseDTO> deleteTicket(@PathVariable String id) {
        return ResponseEntity.ok(supportService.deleteTicket(id));
    }

    //? Admin endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-solved/{id}")
    public ResponseEntity<ResponseDTO> updateSolved(
        @PathVariable String id, 
        @AuthenticationPrincipal UserDetails userDetails, 
        @RequestBody RequestDTO requestDTO
    ) {
        return ResponseEntity.ok(supportService.updateSolved(id, userDetails, requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-unsolved/{id}")
    public ResponseEntity<ResponseDTO> updateUnsolved(
        @PathVariable String id, 
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody RequestDTO requestDTO
    ) {
        return ResponseEntity.ok(supportService.updateUnsolved(id, userDetails, requestDTO));
    }

}
