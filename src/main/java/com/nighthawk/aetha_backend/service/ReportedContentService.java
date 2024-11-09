package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.ReportedContent;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.repository.ReportedContentRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportedContentService {

    @Autowired
    public ReportedContentRepository reportedContentRepository;

    @Autowired
    public PoemRepository poemRepository;

    @Autowired
    public NovelRepository novelRepository;

    @Autowired
    public ResponseDTO responseDTO;

    public ResponseDTO getAllReportedContent(){

        try{
            List<ReportedContent> reportedContents = reportedContentRepository.findAll();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Reported content loaded successfully");
            responseDTO.setContent(reportedContents);
        }catch (Exception e){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Reported content - Error Occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    public ResponseDTO getReportedContentById(String id) {

        try {
            ReportedContent reportedContent = reportedContentRepository.findById(id).orElse(null);

            if (reportedContent != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(reportedContent);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Cannot find the content");
                responseDTO.setContent(id);
            }
        } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error occurred when finding the reported content by ID");

        }

        return responseDTO;
    }

}
