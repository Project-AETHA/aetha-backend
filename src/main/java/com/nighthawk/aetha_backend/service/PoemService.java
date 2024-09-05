package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.Poem;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


// ? methana thamai business logic eka liyanne
// ? service annotation - recognize as a service
@Service
public class PoemService {

    // ? object ekk hadana eka kramayak
    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    ResponseDTO responseDTO;

    public Poem createPoem(Poem poem){
        // TODO Validate poem

        HashMap<String, String> errors = new HashMap<>();

        if(poem.getTitle() == null) {
            errors.put("title", "Title cannot be empty");
        }

        if(errors.isEmpty()) {
            return poemRepository.save(poem);
        } else return null;
    }

    public ResponseDTO getAllPoems() {

        try{

            List<Poem> poems = poemRepository.findAll();

            if(poems.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No poems to display");
            } else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Poems received successfully");
                responseDTO.setContent(poems);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred when fetching poems");
        }

        return responseDTO;
    }

    public ResponseDTO findPoemById(String id) {

        try {
            Poem poem = poemRepository.findById(id).orElse(null);

            if(poem == null) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No poem found by that ID");
            } else {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Poem found");
                responseDTO.setContent(poem);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred when finding the Poem by ID");
        }

        return responseDTO;

    }
}
