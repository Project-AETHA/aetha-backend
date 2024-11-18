package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.MailDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import com.nighthawk.aetha_backend.service.EbookExternalService;
import com.nighthawk.aetha_backend.service.MailService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/ebooks")
public class EbookExternalController {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private EbookExternalService service;

    //? Instantiating KafkaTemplate
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    //! Temp - Mail service instance
    @Autowired
    MailService mailService;
    @Autowired
    private EbookExternalService ebookExternalService;

    @PostMapping("/publish")
    public ResponseEntity<ResponseDTO> publishEbook(
            @ModelAttribute RequestDTO ebook,
            @RequestPart(value = "demoFile", required = false) MultipartFile demoFile,
            @RequestPart(value = "originalFile", required = false) MultipartFile originalFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(service.publishEbook(ebook, demoFile, originalFile, coverImage, userDetails), HttpStatus.OK);
    }

    //? Publishing content to a topic
    @GetMapping("/testing")
    public String testing() {
        kafkaTemplate.send("new-topic", "Hello from Ebook Service");
        return "Ebook Service is up and running";
    }

    //? Listening to a topic
    //? Should be moved to a separate file
    @KafkaListener(topics = "new-topic")
    public void handleNotification() {
        System.out.println("Received notification");

        MailDTO mail = new MailDTO();
        mail.setTo("nipunbathiya1256@gmail.com");
        mail.setSubject("Aetha - Testing");
        mail.setMessage("Testing mailing service from brevo and the kafka pub/sub architecture");

        mailService.sendMail(mail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getBookById(@PathVariable String id) {
        EbookExternal book = service.findBookById(id);

        if(book == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Book not found");
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Book found");
            responseDTO.setContent(book);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllBooks() {
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("All books");
        responseDTO.setContent(service.findAllBooks());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/my-all")
    public ResponseEntity<ResponseDTO> getMyBooks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.findMyBooks(userDetails));
    }

    // ? Deleting the book
    // ? Can only be deleted by the author or an admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteBook(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EbookExternal book = service.findBookById(id);

        if(book == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Book not found");
        } else {
            Boolean deleted = service.deleteBook(book, userDetails);

            if (deleted) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Book deleted successfully");
                responseDTO.setContent(book);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to delete book");
            }

        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // ? Updating the book
    // ? Can only be updated by the author
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateBook(
            @ModelAttribute EbookExternal ebook,
            @RequestPart(value = "demoFile", required = false) MultipartFile demoFile,
            @RequestPart(value = "originalFile", required = false) MultipartFile originalFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ){
        EbookExternal book = service.findBookById(id);

        if(book == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Book not found");
        } else {
            EbookExternal updatedEbook = service.updateBook(id, ebook, userDetails, demoFile, originalFile, coverImage);

            if (updatedEbook != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Book updated successfully");
                responseDTO.setContent(updatedEbook);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to update book");
            }

        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // ? Search by title
    @PostMapping("/search")
    public ResponseEntity<ResponseDTO> searchEbooks(
            @RequestBody RequestDTO requestDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ebookExternalService.searchEbooks(requestDTO, page, pageSize));
    }

}
