package com.nighthawk.aetha_backend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(
            String uploadLocation,
            String fileName,
            MultipartFile multipartFile
    ) throws IOException {

        //* Declaring the path to save the uploaded file using the uploadLocation given as a parameter
        Path uploadPath = Paths.get("C:\\Users\\Nipun BG\\OneDrive - stu.ucsc.cmb.ac.lk\\3rd Year Group Project\\AETHA_Project\\aetha-backend\\src\\main\\resources\\static\\" + uploadLocation);

        //? Checking whether the path exists before saving the file,
        //? If the path doesn't exist, create a new directory according to the upload path
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }
}
