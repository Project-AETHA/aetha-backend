package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.*;
import com.nighthawk.aetha_backend.entity.*;
import com.nighthawk.aetha_backend.repository.*;
import com.nighthawk.aetha_backend.utils.StatusList;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentType;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportedContentService {

    @Autowired
    public ResponseDTO responseDTO;

    @Autowired
    public PoemRepository poemRepository;

    @Autowired
    public NovelRepository novelRepository;

    @Autowired
    public PoemReportedContentRepository poemReportedContentRepository;

    @Autowired
    public NovelReportedContentRepository novelReportedContentRepository;

    @Autowired
    public AuthUserRepository authUserRepository;

    @Autowired
    ModelMapper modelMapper;


    public ResponseDTO getAllGroupedReportedContent(){

        try{
            List<PoemReportedContentSummaryDTO> groupedReportedPoems = poemReportedContentRepository.groupReportsByPoem();
            List<NovelReportedContentSummaryDTO> groupedReportedNovels = novelReportedContentRepository.groupReportsByNovel();

            Map<String, Object> groupedReportedContents = new HashMap<>();
            groupedReportedContents.put("poems",groupedReportedPoems);
            groupedReportedContents.put("novels",groupedReportedNovels);


                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfull");
                responseDTO.setContent(groupedReportedContents);


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

    public ResponseDTO getAllNovelReports(String novelId){

        try{
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));
            List<NovelReportedContent> novelReportedContents = novelReportedContentRepository.findByNovel(novel);

            Map<String, Object> content = new HashMap<>();
            content.put("novel", novel);
            content.put("reportedList", novelReportedContents);

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

    public ResponseDTO getSingleNovelReportDetails(String reportId){
        try{

            NovelReportedContent reportedContent = novelReportedContentRepository.findById(reportId)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + reportId));

            ReportedNovelDTO novelReportedContent = modelMapper.map(reportedContent, ReportedNovelDTO.class);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfull");
            responseDTO.setContent(novelReportedContent);

        } catch (Exception e) {

            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO declineReport(String reportId){

       try{
           NovelReportedContent novelReport = novelReportedContentRepository.findById(reportId).orElse(null);

           if (novelReport != null) {
               novelReport.setStatus(StatusList.DECLINED);
               novelReportedContentRepository.save(novelReport);

               responseDTO.setCode(VarList.RSP_SUCCESS);
               responseDTO.setMessage("Novel report status updated to DECLINED.");
               responseDTO.setContent(novelReport);

               return responseDTO;
           }

           PoemReportedContent poemReport = poemReportedContentRepository.findById(reportId).orElse(null);

           if (poemReport != null) {
               poemReport.setStatus(StatusList.DECLINED);
               poemReportedContentRepository.save(poemReport);

               responseDTO.setCode(VarList.RSP_SUCCESS);
               responseDTO.setMessage("Poem report status updated to DECLINED.");
               responseDTO.setContent(poemReport);

               return responseDTO;
           }

           responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
           responseDTO.setMessage("Reported content not found with ID: " + reportId);
           responseDTO.setContent(null);
           return responseDTO;

       } catch (Exception e){

           responseDTO.setCode(VarList.RSP_FAIL);
           responseDTO.setMessage(e.getMessage());
           responseDTO.setContent(null);

           return responseDTO;
       }
    }

    public ResponseDTO reportPoem(PoemReportedContent poemReportedContent, UserDetails userDetails){

        try{
            AuthUser reporteduser = authUserRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if(reporteduser != null) {

                poemReportedContent.setReportedUser(reporteduser);
                poemReportedContent.setType(ContentType.POEM);

                PoemReportedContent savedReport = poemReportedContentRepository.save(poemReportedContent);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfull");
                responseDTO.setContent(savedReport);

            } else {

                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
            }

        } catch (Exception e){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("error occured");
            responseDTO.setContent(e.getMessage());
        }
        return responseDTO;
    }


    public ResponseDTO reportNovel(NovelReportedContent novelReportedContent, UserDetails userDetails) {

        try{
            AuthUser reporteduser = authUserRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if(reporteduser != null) {

                novelReportedContent.setReporteduser(reporteduser);
                novelReportedContent.setType(ContentType.NOVEL);

                NovelReportedContent savedReport = novelReportedContentRepository.save(novelReportedContent);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfull");
                responseDTO.setContent(savedReport);

            } else {

                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
            }
        } catch (Exception e){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("error occured");
            responseDTO.setContent(e.getMessage());

        }
        return responseDTO;
    }
}
