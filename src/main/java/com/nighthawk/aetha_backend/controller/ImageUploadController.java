package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.utils.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class ImageUploadController {

    private final ResponseDTO responseDTO;

    public ImageUploadController(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> saveImage( @RequestParam("files")MultipartFile[] files ) {

        String uploadLocation = "books";
        Arrays.asList(files).stream().forEach(file -> {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            System.out.println(fileName);

            try {
                FileUploadUtil.saveFile(uploadLocation, fileName, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
