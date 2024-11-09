package com.nighthawk.aetha_backend.controller;


import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.ReportedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/reportedContent")
public class ReportedContentController {

    @Autowired
    public ReportedContentService reportedContentService;

    @Autowired
    public ResponseDTO responseDTO;

    @GetMapping("/get-all-reported-content")
    public ResponseEntity<ResponseDTO> getAllReportedContent(){
        return ResponseEntity.ok(reportedContentService.getAllReportedContent());
    }

    @GetMapping("/get-all-reported-content/{id}")
    public ResponseEntity<ResponseDTO> findContentById(@PathVariable String id){
        return ResponseEntity.ok(reportedContentService.getReportedContentById(id));
    }
}
