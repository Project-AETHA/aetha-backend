package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.EbookFeedbackDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.EbookExternal;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.EbookExternalRepository;
import com.nighthawk.aetha_backend.utils.EncryptionUtil;
import com.nighthawk.aetha_backend.utils.FileUploadUtil;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EbookExternalService {

    @Autowired
    private EbookExternalRepository ebookRepository;

    private final ResponseDTO responseDTO = new ResponseDTO();

    @Autowired
    private AuthUserRepository userRepository;

    private static final String ISBN_PATTERN = "978-\\d{3}-\\d{4}-\\d{2}-\\d{1}";

    @Autowired
    private Environment env;

    private boolean isValidISBN(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_PATTERN);
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches();
    }

    @Transactional
    public ResponseDTO publishEbook(
            RequestDTO ebook,
            MultipartFile demoFile,
            MultipartFile originalFile,
            MultipartFile coverImage,
            UserDetails userDetails
    ) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        EbookExternal newBook = new EbookExternal();
        HashMap<String, String> errors = new HashMap<>();

        try {
            if(user == null) {
                errors.put("userDetails", "User not found");
                throw new Exception("User not found");
            }

            newBook.setAuthor(user);
            newBook.setCreatedAt(new Date());
            newBook.setTitle(ebook.getTitle());
            newBook.setDescription(ebook.getDescription());
            newBook.setGenres(ebook.getGenres());
            newBook.setTags(ebook.getTags());
            newBook.setSold_amount(0);
            newBook.setPrice(Double.valueOf(ebook.getPrice()));

            if (!isValidISBN(ebook.getIsbn())) {
                errors.put("isbn", "Invalid ISBN format");
            } else {
                newBook.setIsbn(ebook.getIsbn());
            }

            String uploadLocation = "ebooks";

            if(coverImage == null) errors.put("coverImage", "Cover image is not given");
            else {
                String uploadLocationCover = "ebooks_covers";
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(coverImage.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("coverImage", "Invalid file extension");
                    throw new Exception("Invalid File extension - Cover Image");
                }

                String customFileName = newBook.getIsbn() + "_cover" + fileExtension;

                try {
                    FileUploadUtil.saveFile(uploadLocationCover, customFileName, coverImage);
                    newBook.setCover_image("/images/" + uploadLocationCover + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("coverImage", "Error saving the cover image");
                }
            }

            if(demoFile == null) errors.put("demoFile", "Demo file not found");
            else {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(demoFile.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("demoFile", "Invalid file extension");
                    throw new Exception("Invalid File extension - Demo File");
                }

                String customFileName = newBook.getIsbn() + "_demo" + fileExtension;

                try {
                    FileUploadUtil.saveFile(uploadLocation, customFileName, demoFile, "documents");
                    newBook.setDemo_loc("/documents/" + uploadLocation + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("demoFile", "Error saving demo file");
                }
            }

            if(originalFile == null) errors.put("originalFile", "Original file not found");
            else {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(originalFile.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("originalFile", "Invalid file extension");
                    throw new Exception("Invalid File extension - Original File");
                }

                String customFileName = newBook.getIsbn() + "_original" + fileExtension;
                SecretKey key = EncryptionUtil.generateKey();
                byte[] encryptedData = EncryptionUtil.encrypt(originalFile.getBytes(), key);

                try {
                    FileUploadUtil.saveFile(uploadLocation, customFileName, encryptedData, "documents");
                    newBook.setOriginal_loc("/documents/" + uploadLocation + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("originalFile", "Error saving original file");
                }

                try {
                    byte[] decryptedData = EncryptionUtil.decrypt(Base64.getEncoder().encodeToString(encryptedData), key);
                    String decryptedFileName = newBook.getIsbn() + "_decrypted" + fileExtension;
                    FileUploadUtil.saveFile(uploadLocation, decryptedFileName, decryptedData, "documents");
                } catch (Exception e) {
                    errors.put("originalFile", "Error decrypting and saving original file");
                }
            }

            if(!errors.isEmpty()) {
                if(newBook.getDemo_loc() != null) {
                    File file = new File("public" + newBook.getDemo_loc());
                    if(file.exists()) file.delete();
                }

                if(newBook.getOriginal_loc() != null) {
                    File file = new File("public" + newBook.getOriginal_loc());
                    if(file.exists()) file.delete();
                }

                throw new Exception("Validation failed");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ebook published successfully");
            responseDTO.setContent(ebookRepository.save(newBook));

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setErrors(errors);
        }

        return responseDTO;
    }

    public EbookExternal findBookById(String id) {
        return ebookRepository.findById(id).orElse(null);
    }

    public List<EbookExternal> findAllBooks() {
        return ebookRepository.findAll();
    }

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

            if(book.getDemo_loc() != null) {
                File file = new File("public" + book.getDemo_loc());
                if(file.exists()) file.delete();
            }

            if(book.getOriginal_loc() != null) {
                File file = new File("public" + book.getOriginal_loc());
                if(file.exists()) file.delete();
            }

            if(book.getCover_image() != null) {
                File file = new File("public" + book.getCover_image());
                if(file.exists()) file.delete();
            }

            ebookRepository.delete(book);
            return true;
        }

        return false;
    }

    @Transactional
    public EbookExternal updateBook(
            String id,
            EbookExternal ebook,
            UserDetails userDetails,
            MultipartFile demoFile,
            MultipartFile originalFile,
            MultipartFile coverImage
    ) {
        EbookExternal book = ebookRepository.findById(id).orElse(null);
        HashMap<String, String> errors = new HashMap<>();
        String uploadLocation = "ebooks";
        String uploadLocationCover = "ebooks_covers";

        if (book == null) {
            return null;
        }

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            return null;
        }

        if (book.getId() != null && ebook.getId() == null) {
            ebook.setId(book.getId());
        }
        if (book.getTitle() != null && ebook.getTitle() == null) {
            ebook.setTitle(book.getTitle());
        }
        if (book.getIsbn() != null && ebook.getIsbn() == null) {
            ebook.setIsbn(book.getIsbn());
        }
        if (book.getGenres() != null && ebook.getGenres() == null) {
            ebook.setGenres(book.getGenres());
        }
        if (book.getTags() != null && ebook.getTags() == null) {
            ebook.setTags(book.getTags());
        }
        if (book.getDescription() != null && ebook.getDescription() == null) {
            ebook.setDescription(book.getDescription());
        }
        if (book.getAuthor() != null) {
            ebook.setAuthor(book.getAuthor());
        }

        if (demoFile != null) {
            if(book.getDemo_loc() != null) {
                File file = new File("public" + book.getDemo_loc());
                if(file.exists()) file.delete();
            }

            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(demoFile.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("demoFile", "Invalid file extension");
            }

            String customFileName = book.getIsbn() + "_demo" + fileExtension;

            try {
                FileUploadUtil.saveFile(uploadLocation, customFileName, demoFile, "documents");
                ebook.setDemo_loc("/documents/" + uploadLocation + "/" + customFileName);
            } catch (IOException e) {
                errors.put("demoFile", "Error saving demo file");
            }
        } else {
            ebook.setDemo_loc(book.getDemo_loc());
        }

        if (originalFile != null) {
            if(book.getOriginal_loc() != null) {
                File file = new File("public" + book.getOriginal_loc());
                if(file.exists()) file.delete();
            }

            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(originalFile.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("originalFile", "Invalid file extension");
            }

            String customFileName = book.getIsbn() + "_original" + fileExtension;

            try {
                FileUploadUtil.saveFile(uploadLocation, customFileName, originalFile, "documents");
                ebook.setOriginal_loc("/documents/" + uploadLocation + "/" + customFileName);
            } catch (IOException e) {
                errors.put("originalFile", "Error saving original file");
            }
        } else {
            ebook.setOriginal_loc(book.getOriginal_loc());
        }

        if (coverImage != null) {
            if(book.getCover_image() != null) {
                File file = new File("public" + book.getCover_image());
                if(file.exists()) file.delete();
            }

            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(coverImage.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("coverImage", "Invalid file extension");
            }

            String customFileName = book.getIsbn() + "_cover" + fileExtension;

            try {
                FileUploadUtil.saveFile(uploadLocationCover, customFileName, coverImage);
                ebook.setCover_image("/images/" + uploadLocation + "/" + customFileName);
            } catch (IOException e) {
                errors.put("coverImage", "Error saving cover image");
            }
        } else {
            ebook.setCover_image(book.getCover_image());
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
            if(!errors.isEmpty()) {
                return null;
            }
            return ebookRepository.save(ebook);
        }

        return null;
    }
}