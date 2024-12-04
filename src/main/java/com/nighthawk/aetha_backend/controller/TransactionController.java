package com.nighthawk.aetha_backend.controller;


import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("get-All-transactions")
    public ResponseEntity<ResponseDTO> getALllTransactions(){

        return ResponseEntity.ok(transactionService.getAllTransactions());

    }
}
