package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.EbookFeedbackDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.EbookExternal;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.EbookExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EbookExternalService {

    @Autowired
    private EbookExternalRepository ebookRepository;

    private final EbookFeedbackDTO feedbackDTO = new EbookFeedbackDTO();
    private final ResponseDTO responseDTO = new ResponseDTO();

    @Autowired
    private AuthUserRepository userRepository;

    public EbookExternal publishEbook(EbookExternal ebook, UserDetails userDetails) {

        // ? Getting the user details of the person currently logged in to publish the ebook
        // ? Return null if the user cannot be found (should not happen)
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return null;
        }

        // ? Setting the author of the ebook to the user who is publishing it
        ebook.setAuthor(user);
        ebook.setCreatedAt(new Date());

        // TODO Validation rules for the ebook
        // TODO document upload handling and encryption decryption process

        return ebookRepository.save(ebook);
    }

    public EbookExternal findBookById(String id) {
        return ebookRepository.findById(id).orElse(null);
    }

    public List<EbookExternal> findAllBooks() {
        return ebookRepository.findAll();
    }

    //? Transactional annotation is not needed here, but might be needed later on
    @Transactional
    public Boolean deleteBook(EbookExternal book, UserDetails userDetails) {
        if (book == null) {
            return false;
        }

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return false;
        }

        if(book.getAuthor().getId().equals(user.getId()) || user.getRole().equals("ADMIN")) {
            // TODO handle file deletion process as well for original and demo versions of the book
            ebookRepository.delete(book);
            return true;
        }

        return false;
    }

    @Transactional
    public EbookExternal updateBook(String id, EbookExternal ebook, UserDetails userDetails) {
        EbookExternal book = ebookRepository.findById(id).orElse(null);

        if (book == null) {
            return null;
        }

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return null;
        }

        // TODO validate the book details before updating

        if (book.getId() != null && ebook.getId() == null) {
            ebook.setId(book.getId());
        }
        if (book.getTitle() != null && ebook.getTitle() == null) {
            ebook.setTitle(book.getTitle());
        }
        if (book.getIsbn() != null && ebook.getIsbn() == null) {
            ebook.setIsbn(book.getIsbn());
        }
        if (book.getAuthor() != null) {
            ebook.setAuthor(book.getAuthor());
        }
        if (book.getDemo_loc() != null && ebook.getDemo_loc() == null) {
            ebook.setDemo_loc(book.getDemo_loc());
        }
        if (book.getOriginal_loc() != null && ebook.getOriginal_loc() == null) {
            ebook.setOriginal_loc(book.getOriginal_loc());
        }
        if (book.getCreatedAt() != null) {
            ebook.setCreatedAt(book.getCreatedAt());
        }
        if (book.getPrice() != null && ebook.getPrice() == null) {
            ebook.setPrice(book.getPrice());
        }
        if (book.getSold_amount() != null && ebook.getSold_amount() == null) {
            ebook.setSold_amount(book.getSold_amount());
        }
        if (book.getFeedback() != null) {
            ebook.setFeedback(book.getFeedback());
        }

        if(book.getAuthor().getId().equals(user.getId())) {
            // TODO handle file update process as well for original and demo versions of the book
            return ebookRepository.save(ebook);
        }

        return null;
    }
}
