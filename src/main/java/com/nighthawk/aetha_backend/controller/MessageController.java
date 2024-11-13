package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.MessageDTO;
import com.nighthawk.aetha_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/inbox/{receiver}")
    public ResponseEntity<List<MessageDTO>> getInboxMessages(@PathVariable String receiver) {
        List<MessageDTO> messages = messageService.getInboxMessages(receiver);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/sent/{sender}")
    public ResponseEntity<List<MessageDTO>> getSentMessages(@PathVariable String sender) {
        List<MessageDTO> messages = messageService.getSentMessages(sender);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/drafts/{sender}")
    public ResponseEntity<List<MessageDTO>> getDraftMessages(@PathVariable String sender) {
        List<MessageDTO> messages = messageService.getDraftMessages(sender);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/trash/{user}")
    public ResponseEntity<List<MessageDTO>> getTrashMessages(@PathVariable String user) {
        List<MessageDTO> messages = messageService.getTrashMessages(user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
