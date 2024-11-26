package com.nighthawk.aetha_backend.controller;


import com.nighthawk.aetha_backend.dto.PoemDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.NovelReportedContent;
import com.nighthawk.aetha_backend.entity.PoemReportedContent;
import com.nighthawk.aetha_backend.service.ReportedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/reportedContent")
public class ReportedContentController {

    @Autowired
    public ReportedContentService reportedContentService;

    @Autowired
    public ResponseDTO responseDTO;


    @GetMapping("/get-all-grouped-reported-contents")
    public ResponseEntity<ResponseDTO> getAllGroupedReportedContent(){
        return ResponseEntity.ok(reportedContentService.getAllGroupedReportedContent());
    }

    @GetMapping("/get-all-poem-reports/{poemId}")
    public ResponseEntity<ResponseDTO> getAllReportedContentForPoem(@PathVariable String poemId){
        return ResponseEntity.ok(reportedContentService.getAllPoemReports(poemId));
    }

    @GetMapping("/get-all-novel-reports/{novelId}")
    public ResponseEntity<ResponseDTO> getAllReportedContentForNovel(@PathVariable String novelId ){
        return ResponseEntity.ok(reportedContentService.getAllNovelReports(novelId));
    }

    @GetMapping("/find-poemreport-details/{reportId}")
    public ResponseEntity<ResponseDTO> getDetailsOfPoemReport(@PathVariable String reportId){
        return ResponseEntity.ok(reportedContentService.getSinglePoemReportDetails(reportId));
    }

    @GetMapping("/find-novelreport-details/{reportId}")
    public ResponseEntity<ResponseDTO> getDetailsOfNovelReport(@PathVariable String reportId){
        return ResponseEntity.ok(reportedContentService.getSingleNovelReportDetails(reportId));
    }

    @PatchMapping("/decline-reportedcontent/{reportId}")
    public ResponseEntity<ResponseDTO> declineReportedContent(@PathVariable String reportId){
        return ResponseEntity.ok(reportedContentService.declineReport(reportId));
    }

    @PostMapping("/report-poem/{poemId}")
    public ResponseEntity<ResponseDTO> reportPoem (
            @RequestBody PoemReportedContent poemReportedContent,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String poemId
    ){
        return ResponseEntity.ok(reportedContentService.reportPoem(poemReportedContent,userDetails,poemId));
    }

    @PostMapping("/report-novel/{novelId}")
    public ResponseEntity<ResponseDTO> reportNovel (
            @RequestBody NovelReportedContent novelReportedContent,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String novelId
    ){
        return ResponseEntity.ok(reportedContentService.reportNovel(novelReportedContent,userDetails,novelId));
    }





}
