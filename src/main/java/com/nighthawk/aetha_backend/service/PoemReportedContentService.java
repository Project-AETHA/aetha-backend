package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.PoemReportedContentSummaryDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.repository.PoemReportedContentRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoemReportedContentService {

    @Autowired
    public ResponseDTO responseDTO;

    @Autowired
    public PoemReportedContentRepository poemReportedContentRepository;

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


}
