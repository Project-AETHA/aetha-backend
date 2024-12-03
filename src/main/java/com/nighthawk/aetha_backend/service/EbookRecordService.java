package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.EbookRecord;
import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.EbookExternalRepository;
import com.nighthawk.aetha_backend.repository.EbookRecordRepository;
import com.nighthawk.aetha_backend.utils.EncryptionUtil;
import com.nighthawk.aetha_backend.utils.FileUploadUtil;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Service
public class EbookRecordService {

    private final ResponseDTO responseDTO;
    private final EbookRecordRepository ebookRecordRepository;
    private final AuthUserRepository userRepository;
    private final EbookExternalRepository ebookRepository;

    public EbookRecordService(
            ResponseDTO responseDTO,
            EbookRecordRepository ebookRecordRepository,
            AuthUserRepository userRepository,
            EbookExternalRepository ebookRepository
    ) {
        this.responseDTO = responseDTO;
        this.ebookRecordRepository = ebookRecordRepository;
        this.userRepository = userRepository;
        this.ebookRepository = ebookRepository;
    }


    public ResponseDTO buyEBook(String ebookId, UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            EbookExternal ebook = ebookRepository.findById(ebookId).orElseThrow(() -> new NoSuchElementException("Ebook not found"));

            EbookRecord previousRecords = ebookRecordRepository.findByEbookAndUser(ebook, user).orElse(null);

            if (previousRecords != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("You have already bought this book");
            } else {
                EbookRecord record = new EbookRecord();
                record.setEbook(ebook);
                record.setUser(user);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Book bought successfully");
                responseDTO.setContent(ebookRecordRepository.save(record));
            }
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Unknown Error");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getMyEbooks(UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Your ebooks");
            responseDTO.setContent(ebookRecordRepository.findAllByUser(user));
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Unknown Error");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO ebookDownload(String ebookId, UserDetails userDetails) {

        HashMap<String, String> errors = new HashMap<>();

        try {

            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            EbookExternal ebook = ebookRepository.findById(ebookId).orElseThrow(() -> new NoSuchElementException("Ebook not found"));

            // Check if the user has purchased the eBook
            EbookRecord previousRecord = ebookRecordRepository.findByEbookAndUser(ebook, user).orElse(null);
            if (previousRecord == null) {
                throw new AccessDeniedException("You have not purchased this eBook.");
            }

            String publicDownloadLocation = "public/downloads/";

            // Get the encrypted file path
            String encryptedFilePath = "public" + ebook.getOriginal_loc();
            File encryptedFile = new File(encryptedFilePath);

            if (!encryptedFile.exists()) {
                errors.put("file", "Ebook file not found.");
                throw new FileNotFoundException("Encrypted file not found.");
            }

            // Decrypt the file
            SecretKey key = EncryptionUtil.generateKey(); // Assume the same key is used for encryption/decryption
            byte[] encryptedData = Files.readAllBytes(encryptedFile.toPath());
            byte[] decryptedData = EncryptionUtil.decrypt(Base64.getEncoder().encodeToString(encryptedData), key);

            // Save the decrypted file to a public location
            String fileExtension = FileUploadUtil.getFileExtension(encryptedFilePath);
            String decryptedFileName = ebook.getIsbn() + "_decrypted" + fileExtension;
            String decryptedFilePath = publicDownloadLocation + decryptedFileName;
            FileUploadUtil.saveFile(publicDownloadLocation, decryptedFileName, decryptedData, "documents");

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Ebook ready for download.");
            responseDTO.setContent("/downloads/" + decryptedFileName);

        } catch (Exception e) {
            errors.put("error", e.getMessage());
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Failed to process the download.");
            responseDTO.setErrors(errors);
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

}
