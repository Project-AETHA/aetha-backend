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


    @GetMapping("/get-all-grouped-reported-poems")
    public ResponseEntity<ResponseDTO> getAllGroupedReportedContent(){
        return ResponseEntity.ok(reportedContentService.getAllGroupedReportedPoems());
    }

    @GetMapping("/get-all-reports/{poemId}")
    public ResponseEntity<ResponseDTO> getAllReportedContentForPoem(@PathVariable String poemId){
        return ResponseEntity.ok(reportedContentService.getAllPoemReports(poemId));
    }

    @GetMapping("/find-poemreport-details/{reportId}")
    public ResponseEntity<ResponseDTO> getDetailsOfPoemReport(@PathVariable String reportId){
        return ResponseEntity.ok(reportedContentService.getSinglePoemReportDetails(reportId));
    }
}
