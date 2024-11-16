package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.PoemReportedContentSummaryDTO;
import com.nighthawk.aetha_backend.dto.ReportedPoemDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.entity.PoemReportedContent;
import com.nighthawk.aetha_backend.repository.PoemReportedContentRepository;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReportedContentService {

    @Autowired
    public ResponseDTO responseDTO;

    @Autowired
    public PoemRepository poemRepository;

    @Autowired
    public PoemReportedContentRepository poemReportedContentRepository;

    @Autowired
    ModelMapper modelMapper;


    public ResponseDTO getAllGroupedReportedPoems(){

        try{
            List<PoemReportedContentSummaryDTO> groupedReportedPoems = poemReportedContentRepository.groupReportsByPoem();

            if(groupedReportedPoems != null && !groupedReportedPoems.isEmpty()){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully send the repored Grouped Poems");
                responseDTO.setContent(groupedReportedPoems);

            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No reported Poems");
                responseDTO.setContent(null);
            }

        } catch (Exception e){
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error Occured");
            responseDTO.setContent(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getAllPoemReports(String poemId){

        try{
            Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new NoSuchElementException("Poem not found"));
            List<PoemReportedContent> poemReportedContents = poemReportedContentRepository.findByPoem(poem);

            Map<String, Object> content = new HashMap<>();
            content.put("poem", poem);
            content.put("reportedList", poemReportedContents);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfull");
            responseDTO.setContent(content);

        } catch (Exception e) {

            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public ResponseDTO getSinglePoemReportDetails(String reportId){

        try{

            PoemReportedContent reportedContent = poemReportedContentRepository.findById(reportId)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + reportId));

            ReportedPoemDTO poemReportedContent = modelMapper.map(reportedContent, ReportedPoemDTO.class);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfull");
            responseDTO.setContent(poemReportedContent);

        } catch (Exception e) {

            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }




}
