package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Blog;
import com.nighthawk.aetha_backend.entity.Book;
import com.nighthawk.aetha_backend.repository.BookRepository;
import com.nighthawk.aetha_backend.service.UserService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final ResponseDTO responseDTO;

    //? Temp
    private final BookRepository repository;
    private final UserService userService;

    public BookController(ResponseDTO responseDTO, BookRepository repository, UserService userService) {
        this.responseDTO = responseDTO;
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {

        try {
            List<Book> books = repository.findAll();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("All the Books");
            responseDTO.setContent(books);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error when getting all the books");
            responseDTO.setContent(null);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping("/add")
    public ResponseEntity<?> addBook(
            @RequestBody Book book,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        System.out.println("Authentic");
        AuthUser user = userService.findByEmail(userDetails.getUsername());

        if(user != null) {
            try {
                book.setAuthor(user);

                repository.save(book);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("The book '" + book.getName() + "' was saved.");
                responseDTO.setContent(book);

            } catch (Exception e) {

                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Saving the book failed");
                responseDTO.setContent(book);
            }
        } else {

        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/single/{name}")
    public ResponseEntity<?> findBookByName(@PathVariable String name) {

        Optional<Book> book = repository.findBookByName(name);

        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Single Book");
        responseDTO.setContent(book);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
