package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/data")
public class DataController {

    //! Constructor injection
    //? Preferred over the autowired injection
    private final DataService dataService;

    @Autowired
    public DataController (DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/clicked/{novelId}")
    public void recordClick(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String novelId
    ) {
        dataService.recordClick(userDetails, novelId);
    }

}
