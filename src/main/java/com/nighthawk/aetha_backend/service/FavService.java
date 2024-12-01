package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.FavPoem;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.FavPoemRepository;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FavService {


    private final ResponseDTO responseDTO;
    private final FavPoemRepository favPoemRepository;
    private final PoemRepository poemRepository;
    private final AuthUserRepository userRepository;

    @Autowired
    public FavService(
            ResponseDTO responseDTO,
            FavPoemRepository favPoemRepository,
            PoemRepository poemRepository,
            AuthUserRepository userRepository
    ) {
        this.responseDTO = responseDTO;
        this.favPoemRepository = favPoemRepository;
        this.poemRepository = poemRepository;
        this.userRepository = userRepository;
    }

    public ResponseDTO addFavPoem(String poemId, UserDetails userDetails) {

        try {
            AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new NoSuchElementException("User not found"));
            Poem poem = poemRepository.findById(poemId).orElseThrow(()-> new NoSuchElementException("Poem not found"));

            FavPoem favPoem = new FavPoem();
            favPoem.setPoem(poem);
            favPoem.setUser(user);

            favPoemRepository.save(favPoem);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Poem added to favourites successfully");
            responseDTO.setContent(favPoem);

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found");
            responseDTO.setContent(e.getMessage());
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

}
