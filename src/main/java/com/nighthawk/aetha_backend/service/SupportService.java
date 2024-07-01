package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.SupportTicket;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.SupportTicketRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SupportService {

    @Autowired
    private SupportTicketRepository supportRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO createTicket(SupportTicket ticket, UserDetails userDetails) {

        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if(user == null){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");

            } else {
                ticket.setCreatedAt(new Date());
                ticket.setAuthor(user);
                supportRepository.save(ticket);
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ticket created successfully");
                responseDTO.setContent(ticket);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while creating ticket");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    public ResponseDTO getAllTickets() {
        try {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Tickets retrieved successfully");
            responseDTO.setContent(supportRepository.findAll());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while retrieving tickets");
        }

        return responseDTO;
    }

}
