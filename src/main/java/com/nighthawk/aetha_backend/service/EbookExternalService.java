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

    private final EbookFeedbackDTO feedbackDTO = new EbookFeedbackDTO();
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
        // ? Getting the user details of the person currently logged in to publish the ebook
        // ? Return null if the user cannot be found (should not happen)

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        EbookExternal newBook = new EbookExternal();

        // ? Hashmap to store the errors that occur during the validation process
        HashMap<String, String> errors = new HashMap<String, String>();

        try {
            if(user == null) {
                errors.put("userDetails", "User not found");
                throw new Exception("User not found");
            }

            // ? Setting the author of the ebook to the user who is publishing it
            newBook.setAuthor(user);
            newBook.setCreatedAt(new Date());
            newBook.setTitle(ebook.getTitle());
            newBook.setSold_amount(0);
            newBook.setPrice(ebook.getPrice());

            if (!isValidISBN(ebook.getIsbn())) {
                errors.put("isbn", "Invalid ISBN format");
            } else {
                newBook.setIsbn(ebook.getIsbn());
            }

            // TODO document encryption decryption process

            // ? Setting the location where the files will be uploaded
            String uploadLocation = "ebooks";

            // ? Handling the cover image uploading process
            if(coverImage == null) errors.put("coverImage", "Cover image is not given");
            else {
                String uploadLocationCover = "ebooks_covers";

                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(coverImage.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("coverImage", "Invalid file extension");
                    throw new Exception("Invalid File extension - Cover Image");
                }

                //? Create a new file name with the custom prefix and the original extension
                String customFileName = newBook.getIsbn() + "_cover" + fileExtension;

                // ? Saving the files on the storage
                try {
                    FileUploadUtil.saveFile(uploadLocationCover, customFileName, coverImage);
                    newBook.setCover_image("/images/" + uploadLocationCover + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("coverImage", "Error saving the cover image");
                }
            }

            // ? Handling file uploading and saving the file location to the database for demo file
            if(demoFile == null) errors.put("demoFile", "Demo file not found");
            else {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(demoFile.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("demoFile", "Invalid file extension");
                    throw new Exception("Invalid File extension - Demo File");
                }

                //? Create a new file name with the custom prefix and the original extension
                String customFileName = newBook.getIsbn() + "_demo" + fileExtension;

                // ? Saving the files on the storage
                try {
                    FileUploadUtil.saveFile(uploadLocation, customFileName, demoFile, "documents");
                    newBook.setDemo_loc("/documents/" + uploadLocation + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("demoFile", "IError saving demo file");
                }
            }

            // ? Handling file uploading and saving the file location to the database for original file
            if(originalFile == null) errors.put("originalFile", "Original file not found");
            else {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(originalFile.getOriginalFilename()));
                String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

                if(fileExtension == null) {
                    errors.put("originalFile", "Invalid file extension");
                    throw new Exception("Invalid File extension - Original File");
                }

                //? Create a new file name with the custom prefix and the original extension
                String customFileName = newBook.getIsbn() + "_original" + fileExtension;

                //? Encrypting the original file
                SecretKey key = EncryptionUtil.generateKey();
                byte[] encryptedData = EncryptionUtil.encrypt(originalFile.getBytes(), key);

                // ? Saving the files on the storage
                try {
                    FileUploadUtil.saveFile(uploadLocation, customFileName, encryptedData, "documents");
                    newBook.setOriginal_loc("/documents/" + uploadLocation + "/" + customFileName);
                } catch (IOException e) {
                    errors.put("originalFile", "Error saving original file");
                }

//                // ? Saving the decrypted file for testing purpose
//                try {
//                    byte[] decryptedData = EncryptionUtil.decrypt(Base64.getEncoder().encodeToString(encryptedData), key);
//                    String decryptedFileName = newBook.getIsbn() + "_decrypted" + fileExtension;
//
//                    // Saving the decrypted file
//                    FileUploadUtil.saveFile(uploadLocation, decryptedFileName, decryptedData, "documents");
//                } catch (Exception e) {
//                    errors.put("originalFile", "Error decrypting and saving original file");
//                }
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

        HashMap<String, String> errors = new HashMap<String, String>();

        // ? Setting the location where the files will be uploaded
        String uploadLocation = "ebooks";
        String uploadLocationCover = "ebooks_covers";

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

        if (demoFile != null) {

            //? Deleting the previously stored file
            if(book.getDemo_loc() != null) {
                File file = new File("public" + book.getDemo_loc());
                if(file.exists()) file.delete();
            }

            //? Uploading the new file
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(demoFile.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("demoFile", "Invalid file extension");
            }

            //? Create a new file name with the custom prefix and the original extension
            String customFileName = book.getIsbn() + "_demo" + fileExtension;

            // ? Saving the files on the storage
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
            //? Deleting the previously stored file
            if(book.getOriginal_loc() != null) {
                File file = new File("public" + book.getOriginal_loc());
                if(file.exists()) file.delete();
            }

            //? Uploading the new file
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(originalFile.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("originalFile", "Invalid file extension");
            }

            //? Create a new file name with the custom prefix and the original extension
            String customFileName = book.getIsbn() + "_original" + fileExtension;

            // ? Saving the files on the storage
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
            //? Deleting the previously stored file
            if(book.getCover_image() != null) {
                File file = new File("public" + book.getCover_image());
                if(file.exists()) file.delete();
            }

            //? Uploading the new file
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(coverImage.getOriginalFilename()));
            String fileExtension = FileUploadUtil.getFileExtension(originalFileName);

            if(fileExtension == null) {
                errors.put("coverImage", "Invalid file extension");
            }

            //? Create a new file name with the custom prefix and the original extension
            String customFileName = book.getIsbn() + "_cover" + fileExtension;

            // ? Saving the files on the storage
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
