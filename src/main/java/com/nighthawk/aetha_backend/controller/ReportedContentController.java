package com.nighthawk.aetha_backend.controller;


import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.PoemReportedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/reportedContent")
public class ReportedContentController {

    @Autowired
    public PoemReportedContentService poemReportedContentService;

    @Autowired
    public ResponseDTO responseDTO;


    @GetMapping("/get-all-grouped-reported-poems")
    public ResponseEntity<ResponseDTO> getAllGroupedReportedContent(){
        return ResponseEntity.ok(poemReportedContentService.getAllGroupedReportedPoems());
    }
}
