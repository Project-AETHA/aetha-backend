package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.SupportTicket;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.SupportTicketRepository;
import com.nighthawk.aetha_backend.utils.FileUploadUtil;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SupportService {

    @Autowired
    private SupportTicketRepository supportRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO createTicket(
            String title,
            String category,
            String description,
            MultipartFile[] files,
            UserDetails userDetails
    ) {

        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            SupportTicket ticket = new SupportTicket();

            if(user == null){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");

            } else {
                ticket.setTitle(title);
                ticket.setCategory(category);
                ticket.setDescription(description);
                ticket.setCreatedAt(new Date());
                ticket.setAuthor(user);

                //? Saving the complaint ticket object without the attachments(files)
                ticket = supportRepository.save(ticket);
                System.out.println(files);
                if(files != null) {
                    List<String> initialAttachments = new ArrayList<>();

                    String uploadLocation = "complaints";
                    // Iterate over the files with an index
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];

                        //? Extract the original file name and extension
                        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                        String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                        if(fileExtension == null) {
                            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                            responseDTO.setMessage("Invalid file extension");
                            return responseDTO;
                        }

                        //? Create a new file name with the custom prefix and the original extension
                        String customFileName = ticket.getId() + "_" + (i + 1) + fileExtension;

                        try {
                            FileUploadUtil.saveFile(uploadLocation, customFileName, file);
                            initialAttachments.add("/images/" + uploadLocation + "/" + customFileName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //? Adding the initial attachments to the ticket
                    ticket.setAttachments(initialAttachments);
                    System.out.println("Attachments: " + initialAttachments);
                }

                //? Updating the saved ticket with the uploaded file information
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

    public ResponseDTO getTicketByEmail(UserDetails userDetails) {
        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if(user != null) {
                List<SupportTicket> tickets = supportRepository.findByAuthor(user);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Tickets retrieved successfully");
                responseDTO.setContent(tickets);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while retrieving tickets");
        }

        return responseDTO;
    }

    public ResponseDTO getTicketById (String id) {
        try {
            Optional<SupportTicket> ticket = supportRepository.findById(id);

            if(ticket.isPresent()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ticket retrieved successfully");
                responseDTO.setContent(ticket.get());
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Ticket not found");
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while retrieving ticket");
        }

        return responseDTO;
    }

    public ResponseDTO updateTicket(String title, String category, String description, MultipartFile[] files, UserDetails userDetails, String id) {
        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            SupportTicket ticket = supportRepository.findById(id).orElse(null);

            if(user == null){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");

            } else if(ticket == null) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Ticket not found");
            } else {
                ticket.setTitle(title);
                ticket.setCategory(category);
                ticket.setDescription(description);

                //? Saving the complaint ticket object without the attachments(files)
                ticket = supportRepository.save(ticket);
                if(files != null) {
                    List<String> initialAttachments = new ArrayList<>();

                    String uploadLocation = "complaints";
                    // Iterate over the files with an index
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];

                        //? Extract the original file name and extension
                        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                        String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                        if(fileExtension == null) {
                            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                            responseDTO.setMessage("Invalid file extension");
                            return responseDTO;
                        }

                        //? Create a new file name with the custom prefix and the original extension
                        String customFileName = ticket.getId() + "_" + (i + 1) + fileExtension;

                        try {
                            FileUploadUtil.saveFile(uploadLocation, customFileName, file);
                            initialAttachments.add("/images/" + uploadLocation + "/" + customFileName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //? Adding the initial attachments to the ticket
                    ticket.setAttachments(initialAttachments);
                    System.out.println("Attachments: " + initialAttachments);

                    //? Updating the saved ticket with the uploaded file information
                    supportRepository.save(ticket);
                }

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ticket updated successfully");
                responseDTO.setContent(ticket);
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while updating the ticket");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO deleteTicket(String id) {
        try {
            Optional<SupportTicket> ticketOptional = supportRepository.findById(id);
            if (ticketOptional.isPresent()) {
                SupportTicket ticket = ticketOptional.get();
                List<String> attachments = ticket.getAttachments();

                if (attachments != null) {
                    for (String attachment : attachments) {
                        String filePath = "public" + attachment;
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                supportRepository.deleteById(id);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ticket and associated files deleted successfully");
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Ticket not found");
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("An error occurred while deleting the ticket");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
