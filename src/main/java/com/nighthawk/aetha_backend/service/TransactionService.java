package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.AdDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Ad;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.repository.AdRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    AdRepository adRepository;

    @Autowired
    ResponseDTO responseDTO;

    public ResponseDTO getAllTransactions() {

        try{

            List<Ad> transactions = adRepository.findAll();

            if(transactions.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No transactions to display");
            } else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("successful");
                responseDTO.setContent(transactions);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred when fetching poems");
        }

        return responseDTO;
    }
}
