package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.EbookExternal;
import com.nighthawk.aetha_backend.service.EbookExternalService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/ebooks")
public class EbookExternalController {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private EbookExternalService service;

    @PostMapping("/publish")
    public ResponseEntity<ResponseDTO> publishEbook(
            @RequestBody EbookExternal ebook,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        EbookExternal savedBook = service.publishEbook(ebook, userDetails);

        if(savedBook == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Failed to publish ebook");
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ebook published successfully");
            responseDTO.setContent(ebook);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
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
            @RequestBody EbookExternal ebook,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ){
        EbookExternal book = service.findBookById(id);

        if(book == null) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Book not found");
        } else {
            EbookExternal updatedEbook = service.updateBook(id, ebook, userDetails);

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

}