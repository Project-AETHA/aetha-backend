package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.*;
import com.nighthawk.aetha_backend.repository.*;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FavService {

    private final ResponseDTO responseDTO;
    private final FavPoemRepository favPoemRepository;
    private final FavNovelRepository favNovelRepository;
    private final PoemRepository poemRepository;
    private final NovelRepository novelRepository;
    private final AuthUserRepository userRepository;

    @Autowired
    public FavService(
            ResponseDTO responseDTO,
            FavPoemRepository favPoemRepository,
            PoemRepository poemRepository,
            AuthUserRepository userRepository,
            NovelRepository novelRepository,
            FavNovelRepository favNovelRepository
    ) {
        this.responseDTO = responseDTO;
        this.favPoemRepository = favPoemRepository;
        this.poemRepository = poemRepository;
        this.userRepository = userRepository;
        this.novelRepository = novelRepository;
        this.favNovelRepository = favNovelRepository;
    }

    public ResponseDTO addFavPoem(String poemId, UserDetails userDetails, boolean setFav) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new NoSuchElementException("Poem not found"));

            FavPoem previousFavPoem = favPoemRepository.findByPoemAndUser(poem, user);

            if (setFav) {
                if (previousFavPoem != null) throw new IllegalArgumentException("Poem already added to favourites");

                FavPoem favPoem = new FavPoem();
                favPoem.setPoem(poem);
                favPoem.setUser(user);

                favPoemRepository.save(favPoem);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Poem added/removed to favourites successfully");
                responseDTO.setContent(favPoem);
            } else {

                favPoemRepository.delete(previousFavPoem);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Poem added/removed to favourites successfully");
                responseDTO.setContent(previousFavPoem);
            }


        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }


    public ResponseDTO addFavNovel(String novelId, UserDetails userDetails, boolean setFav) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            FavNovel previousFavNovel = favNovelRepository.findByNovelAndUser(novel, user).orElse(null);

            if (setFav) {
                if (previousFavNovel != null) throw new IllegalArgumentException("Novel already added to favourites");

                FavNovel favNovel = new FavNovel();
                favNovel.setNovel(novel);
                favNovel.setUser(user);

                favNovelRepository.save(favNovel);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Novel added/removed to favourites successfully");
                responseDTO.setContent(favNovel);
            } else {

                favNovelRepository.delete(previousFavNovel);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Novel added/removed to favourites successfully");
                responseDTO.setContent(previousFavNovel);
            }


        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO isFavNovel(String novelId, UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            FavNovel favNovel = favNovelRepository.findByNovelAndUser(novel, user).orElse(null);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data found");
            responseDTO.setContent(favNovel != null);

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO isFavPoem(String poemId, UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new NoSuchElementException("Poem not found"));

            FavPoem favPoem = favPoemRepository.findByPoemAndUser(poem, user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data found");
            responseDTO.setContent(favPoem != null);

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getMyFavNovels(UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data found");
            responseDTO.setContent(favNovelRepository.findByUser(user));

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getMyFavPoems(UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data found");
            responseDTO.setContent(favPoemRepository.findByUser(user));

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Data not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
