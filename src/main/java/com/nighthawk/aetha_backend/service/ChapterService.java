package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ChapterDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Chapter;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.SubscriptionTiers;
import com.nighthawk.aetha_backend.repository.*;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.ContentStatus;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ChapterService {


    private final ClicksRepository clicksRepository;
    private final ResponseDTO responseDTO;
    private final ChapterRepository chapterRepository;
    private final NovelRepository novelRepository;
    private final AuthUserRepository authUserRepository;
    private final ModelMapper modelMapper;
    private final SubscriptionTiersRepository subscriptionTiersRepository;

    @Autowired
    public ChapterService (
            ResponseDTO responseDTO,
            ChapterRepository chapterRepository,
            NovelRepository novelRepository,
            AuthUserRepository authUserRepository,
            ModelMapper modelMapper,
            ClicksRepository clicksRepository, SubscriptionTiersRepository subscriptionTiersRepository) {
        this.responseDTO = responseDTO;
        this.chapterRepository = chapterRepository;
        this.novelRepository = novelRepository;
        this.authUserRepository = authUserRepository;
        this.modelMapper = modelMapper;
        this.clicksRepository = clicksRepository;
        this.subscriptionTiersRepository = subscriptionTiersRepository;
    }

    //* Getting all the chapters by the novelId given
    public ResponseDTO getAllChaptersByNovelIdAndStatusAndVisibility(String novelId) {

        try {
            //? Get the novel related to the chapters
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchBeanDefinitionException("Novel not found"));

            //? Get all chapters related to the novel
            List<ChapterDTO> chapterDTOS = modelMapper
                    .map(
                            chapterRepository.findAllByNovelAndStatusAndIsVisible(novel, "COMPLETED", true), //* Mapping the resulting Chapter List
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

    public ResponseDTO createChapter(String novelId, Boolean isComplete, Chapter chapter, UserDetails userDetails) {

        try {
            //? Get the novel related to the chapters
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            //? Validating the user
            if(!novel.getAuthor().getUsername().equals(userDetails.getUsername())) {
                throw new NoSuchElementException("User not authorized to create chapter");
            }

            //? Setting the novel to the chapter
            chapter.setNovel(novel);

            if(isComplete) {
                chapter.setStatus(ContentStatus.COMPLETED);
            } else {
                chapter.setStatus(ContentStatus.DRAFT);
                chapter.setIsVisible(false);
            }

            //? Validating the chapter (title is required)
            if(chapter.getTitle() == null || chapter.getTitle().isEmpty()) {
                throw new NoSuchElementException("Title is required");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Chapter created successfully");
            responseDTO.setContent(chapterRepository.save(chapter));
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Validation failed");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO updateChapterVisibility(String chapterId, Boolean isVisible, UserDetails userDetails) {

            try {
                //? Get the chapter by the chapterId given
                Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NoSuchBeanDefinitionException("Chapter not found"));

                //? Validating the user
                if(!chapter.getNovel().getAuthor().getUsername().equals(userDetails.getUsername())) {
                    throw new NoSuchElementException("User not authorized to update chapter");
                }

                //? Setting the visibility
                chapter.setIsVisible(isVisible);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Chapter visibility updated successfully");
                responseDTO.setContent(chapterRepository.save(chapter));
            } catch (NoSuchElementException e) {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(e.getMessage());
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error occurred");
                responseDTO.setContent(e.getMessage());
            }

            return responseDTO;
    }

    public ResponseDTO updateChapter(String chapterId, Chapter chapter, UserDetails userDetails) {

            try {
                //? Get the chapter by the chapterId given
                Chapter existingChapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NoSuchElementException("Chapter not found"));

                //? Validating the user
                if(!existingChapter.getNovel().getAuthor().getUsername().equals(userDetails.getUsername())) {
                    throw new NoSuchElementException("User not authorized to update chapter");
                }

                //? Setting the content
                if(chapter.getTitle() != null) existingChapter.setTitle(chapter.getTitle());
                if(chapter.getChapterNumber() != null) existingChapter.setChapterNumber(chapter.getChapterNumber());
                if(chapter.getContent() != null) existingChapter.setContent(chapter.getContent());
                if(chapter.getRate() != null) existingChapter.setRate(chapter.getRate());

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Chapter content updated successfully");
                responseDTO.setContent(chapterRepository.save(existingChapter));
            } catch (NoSuchElementException e) {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage("Validation failed");
                responseDTO.setContent(e.getMessage());
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error occurred");
                responseDTO.setContent(e.getMessage());
            }

            return responseDTO;
    }

    public ResponseDTO getAllChaptersByNovelId(String novelId, UserDetails userDetails) {

            try {
                //? Get the novel related to the chapters
                Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

                //? Validating the user
                if(!novel.getAuthor().getUsername().equals(userDetails.getUsername())) {
                    throw new NoSuchElementException("User not authorized to fetch chapters");
                }

                //? Get all chapters related to the novel
                List<ChapterDTO> chapterDTOS = modelMapper
                        .map(
                                chapterRepository.findByNovel(novel), //* Mapping the resulting Chapter List
                                new TypeToken<List<ChapterDTO>>(){}.getType() //* To a list of ChapterDTO
                        );

                //? Setting up the responses
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Chapters fetched successfully");
                responseDTO.setContent(chapterDTOS);

            } catch (NoSuchElementException e) {
                responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
                responseDTO.setMessage(e.getMessage());
                responseDTO.setContent(novelId);
            } catch (Exception e) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error occurred");
                responseDTO.setContent(e.getMessage());
            }

            return responseDTO;
    }

    public ResponseDTO getChapterByNovelIdAndChapterNumber(String novelId, Integer chapterNumber) {
        try {
            Chapter chapter = chapterRepository.findByNovelIdAndChapterNumber(novelId, chapterNumber).orElseThrow(() -> new NoSuchElementException("Chapter not found"));
            int totalChapterCount = chapterRepository.countChaptersByNovelIdAndIsVisibleAndStatus(novelId, true, String.valueOf(ContentStatus.COMPLETED));

            chapter.setTotalChapterCount(totalChapterCount);

            //? Setting up the responses
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Chapters fetched successfully");
            responseDTO.setContent(chapter);
        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(novelId);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getChapterManagementData(String novelId, UserDetails userDetails) {

        try {
            AuthUser author = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            if(!author.equals(novel.getAuthor())) {
                throw new NoSuchElementException("User not authorized");
            }

            List<Chapter> chapters = chapterRepository.findByNovel(novel);
            int totalClicks = clicksRepository.countClicksByNovel(novel);
            SubscriptionTiers tiers = subscriptionTiersRepository.findByNovelId(novel.getId()).orElseThrow(() -> new NoSuchElementException("Subscription tiers not found"));
            tiers.setNovel(novel);

            HashMap<String, Object> data = new HashMap<>();
            data.put("chapters", chapters);
            data.put("clicks", totalClicks);
            data.put("tiers", tiers);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data fetched successfully");
            responseDTO.setContent(data);

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
