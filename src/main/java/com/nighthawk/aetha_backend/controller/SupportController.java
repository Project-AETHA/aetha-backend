package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.SupportType;
import com.nighthawk.aetha_backend.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/support/create_ticket")
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

    @PostMapping("/support/update_ticket/{id}")
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

    @GetMapping("/support/get_all_tickets")
    public ResponseEntity<ResponseDTO> getAllTickets() {
        return ResponseEntity.ok(supportService.getAllTickets());
    }

    @GetMapping("/support/get_my_tickets")
    public ResponseEntity<ResponseDTO> getMyTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(supportService.getTicketByEmail(userDetails));
    }

    @GetMapping("/support/get_ticket/{id}")
    public ResponseEntity<ResponseDTO> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(supportService.getTicketById(id));
    }

    @DeleteMapping("/support/delete_ticket/{id}")
    public ResponseEntity<ResponseDTO> deleteTicket(@PathVariable String id) {
        return ResponseEntity.ok(supportService.deleteTicket(id));
    }

}
