package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ChapterDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.ChapterRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    ResponseDTO responseDTO;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    NovelRepository novelRepository;

    @Autowired
    ModelMapper modelMapper;

    //* Getting all the chapters by the novelId given
    public ResponseDTO getAllChaptersByNovelId(String novelId) {

        try {
            //? Get the novel related to the chapters
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchBeanDefinitionException("Novel not found"));

            //? Get all chapters related to the novel
            List<ChapterDTO> chapterDTOS = modelMapper
                    .map(
                            chapterRepository.findAllByNovel(novel), //* Mapping the resulting Chapter List
                            new TypeToken<List<ChapterDTO>>(){}.getType() //* To a list of ChapterDTO
                    );

            //? Setting up the responses
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Chapters fetched successfully");
            responseDTO.setContent(chapterDTOS);

        } catch (NoSuchBeanDefinitionException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(novelId);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    //* Getting a single chapter by the chapterId given
    public ResponseDTO getChapterById(String chapterId) {

        try {
            //? Get the chapter by the chapterId given
            Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NoSuchBeanDefinitionException("Chapter not found"));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Chapter fetched successfully");
            //? Here we are sending the Chapter instead of ChapterDTO since DTO doesn't have the content attribute
            responseDTO.setContent(chapter);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
