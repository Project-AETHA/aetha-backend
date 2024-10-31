package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController (HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/viewed/{novelId}")
    public void addHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String novelId
    ) {
        historyService.addHistory(userDetails, novelId);
    }

}
