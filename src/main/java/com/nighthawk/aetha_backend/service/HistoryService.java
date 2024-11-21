package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Click;
import com.nighthawk.aetha_backend.entity.History;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.ClicksRepository;
import com.nighthawk.aetha_backend.repository.HistoryRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
@Slf4j
public class HistoryService {

    Logger logger = Logger.getLogger(HistoryService.class.getName());

    private final HistoryRepository historyRepository;
    private final AuthUserRepository authUserRepository;
    private final NovelRepository novelRepository;

    @Autowired
    public HistoryService (AuthUserRepository authUserRepository, NovelRepository novelRepository, HistoryRepository historyRepository) {
        this.authUserRepository = authUserRepository;
        this.novelRepository = novelRepository;
        this.historyRepository = historyRepository;
    }

    public void addHistory(UserDetails userDetails, String novelId) {
        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            //? This is possible due to the Builder annotation in the Entity
            History history = History.builder()
                    .novel(novel)
                    .user(user)
                    .createdAt(new Date())
                    .build();

            historyRepository.save(history);

        } catch (NoSuchElementException e) {
            logger.warning("User not found, click was not recorded. User -> " + userDetails.getUsername());
        }
    }

}
