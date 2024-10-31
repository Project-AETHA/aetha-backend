package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Click;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.ClicksRepository;
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
public class DataService {

    Logger logger = Logger.getLogger(DataService.class.getName());

    private final AuthUserRepository authUserRepository;
    private final NovelRepository novelRepository;
    private final ClicksRepository clicksRepository;

    @Autowired
    public DataService (AuthUserRepository authUserRepository, NovelRepository novelRepository, ClicksRepository clicksRepository) {
        this.authUserRepository = authUserRepository;
        this.novelRepository = novelRepository;
        this.clicksRepository = clicksRepository;
    }


    public void recordClick(UserDetails userDetails, String novelId) {
        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            //? This is possible due to the Builder annotation in the Entity
            Click click = Click.builder()
                    .novel(novel)
                    .user(user)
                    .createdAt(new Date())
                    .build();

            clicksRepository.save(click);

        } catch (NoSuchElementException e) {
            logger.warning("User not found, click was not recorded. User -> " + userDetails.getUsername());
        }
    }

}
