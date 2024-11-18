package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.EbookExternalDTO;
import com.nighthawk.aetha_backend.dto.RequestDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.ebook.EbookExternal;
import com.nighthawk.aetha_backend.entity.Genres;
import com.nighthawk.aetha_backend.entity.Tags;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.EbookExternalRepository;
import com.nighthawk.aetha_backend.repository.SearchRepository;
import com.nighthawk.aetha_backend.utils.EncryptionUtil;
import com.nighthawk.aetha_backend.utils.FileUploadUtil;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.util.stream.Collectors;

@Service
public class EbookExternalService {

    @Autowired
    private EbookExternalRepository ebookRepository;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private SearchRepository searchRepository;

    private static final String ISBN_PATTERN = "978-\\d{3}-\\d{4}-\\d{2}-\\d{1}";

    @Autowired
    private Environment env;
    @Autowired
    private MongoTemplate mongoTemplate;

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

            if(ebook.getTitle() == null) {
                errors.put("title", "Title cannot be empty");
            }


            // * Mapping genre strings to enum
            if(ebook.getGenres() != null) {
                List<Genres> genreList = new ArrayList<>();
                for (String genreString : ebook.getGenres()) {
                    String trimmedGenre = genreString.trim().toUpperCase();
                    try {
                        Genres genre = Genres.valueOf(trimmedGenre);
                        genreList.add(genre);
                    } catch (IllegalArgumentException e) {
                        errors.put("genres", "Invalid genre: " + trimmedGenre);
                    }
                }
                newBook.setGenres(genreList);
            } else {
                errors.put("genres", "Genres cannot be empty");
            }


            // * Mapping tags strings to Tags enum
            if(ebook.getTags() != null) {
                List<Tags> tagsList = new ArrayList<>();
                for (String tagString : ebook.getTags()) {
                    String trimmedTag = tagString.trim().toUpperCase();
                    try {
                        Tags tag = Tags.valueOf(trimmedTag);
                        tagsList.add(tag);
                    } catch (IllegalArgumentException e) {
                        errors.put("tags", "Invalid tag: " + trimmedTag);
                    }
                }
                newBook.setTags(tagsList);
            } else {
                errors.put("tags", "Empty tags");
            }

            // Convert custom_tags to uppercase
            if(ebook.getCustomTags() != null) {
                List<String> customTagsUpper = ebook.getCustomTags().stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
                newBook.setCustom_tags(customTagsUpper);
            }

            newBook.setSold_amount(0);
            newBook.setPrice(Double.valueOf(ebook.getPrice()));
            newBook.setStatus(ContentStatus.PENDING);

            if (!isValidISBN(ebook.getIsbn())) {
                errors.put("isbn", "Invalid ISBN format Eg: 978-123-1234-12-1");
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
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setErrors(errors);
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public EbookExternal findBookById(String id) {
        return ebookRepository.findById(id).orElse(null);
    }

    public List<EbookExternalDTO> findAllBooks() {

        List<EbookExternal> ebooks = ebookRepository.findAll();

        // ! TODO - Unknown Content
        return ebooks.stream()
                .map(ebook -> modelMapper.typeMap(EbookExternal.class, EbookExternalDTO.class)
                        .addMappings(mapper -> mapper.map(src -> src.getAuthor().getDisplayName(), EbookExternalDTO::setAuthor))
                        .map(ebook))
                .collect(Collectors.toList());
    }

    public ResponseDTO findMyBooks(UserDetails userDetails) {
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user == null) {
            responseDTO.setCode(VarList.RSP_TOKEN_INVALID);
            responseDTO.setMessage("User not found");
            responseDTO.setContent(null);
        } else {
            try {
                List<EbookExternal> ebooks = ebookRepository.findByAuthor(user);

                List<EbookExternalDTO> myEbooks = ebooks.stream()
                        .map(ebook -> modelMapper.typeMap(EbookExternal.class, EbookExternalDTO.class)
                                .addMappings(mapper -> mapper.map(src -> src.getAuthor().getDisplayName(), EbookExternalDTO::setAuthor))
                                .map(ebook))
                        .toList();

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(myEbooks);

                if(myEbooks.isEmpty()) responseDTO.setMessage("No books found");
                else responseDTO.setMessage("Books found");

            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error fetching books");
                responseDTO.setContent(null);
            }
        }

        return responseDTO;
    }

    @Transactional
    public ResponseDTO searchEbooks(RequestDTO requestDTO, int page, int pageSize) {

        try {
            // Create pagination object
//            Pageable pageable = PageRequest.of(page, pageSize);

            // Create query object
            Query query = new Query();

            // Add search criteria
            if (requestDTO.getSearchTerm() != null && !requestDTO.getSearchTerm().isEmpty()) {
                query.addCriteria(Criteria.where("title").regex(".*" + Pattern.quote(requestDTO.getSearchTerm().toLowerCase()) + ".*", "i"));
            }
            if (requestDTO.getRating() != null) {
                query.addCriteria(Criteria.where("rating").gte(requestDTO.getRating()));
            }

            // Add pagination to query
//            query.with(pageable);

            // Execute query
            List<EbookExternal> ebooks = mongoTemplate.find(query, EbookExternal.class);

            // Check if results are empty
            if (ebooks.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No ebooks found for the provided criteria");
                responseDTO.setContent(null);
            } else {
                // Prepare response for successful fetch
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Ebooks fetched successfully");
                responseDTO.setContent(ebooks);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
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

        if (book.getStatus() != null) {
            ebook.setStatus(book.getStatus());
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