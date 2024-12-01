package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.PoemDTO;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.*;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.repository.VoteRecordRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private AuthUserRepository userRepository;

    @Autowired
    private VoteRecordRepository voteRecordRepository;

    @Autowired
    ResponseDTO responseDTO;

    @Autowired
    ModelMapper modelMapper;

    public ResponseDTO createPoem(PoemDTO poem, UserDetails userDetails){

        // ? Fetching user data
        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(user != null) {
            poem.setAuthor(user);
        }

        poem.setCreated_At(new Date());
        poem.setClicks(0);
        poem.setUpvotes(0);
        poem.setDownvotes(0);
        poem.setViews(0);
        poem.setVisibility(true);


        // TODO Validate poem

        HashMap<String, String> errors = new HashMap<>();

        if(poem.getTitle() == null) { errors.put("title", "Title cannot be empty"); }

        if(poem.getContent() == null) { errors.put("content", "The content cannot be empty"); }

        if(poem.getContent().length()<20) {errors.put("ContentLength", "The content length is too small");}

        Poem newPoem = modelMapper.map(poem, Poem.class);

        if(errors.isEmpty()) {
            responseDTO.setContent(poemRepository.save(newPoem));
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Poem saved successfully");
        } else {
            responseDTO.setCode(VarList.RSP_VALIDATION_FAILED);
            responseDTO.setMessage("Validation failed");
            responseDTO.setContent(null);
            responseDTO.setErrors(errors);
        }

        return responseDTO;
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

    public ResponseDTO deletePoem(String id){

        // TODO - Only the author or the admin can delete a poem

        poemRepository.deleteById(id);
        Poem poem = poemRepository.findById(id).orElse(null);
        if(poem == null) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setContent(null);
            responseDTO.setMessage("Poem deleted Successfully");
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setContent(poem);
            responseDTO.setMessage("Poem deletion Failed");
        }

        return  responseDTO;
    }

    @Transactional
    public ResponseDTO updatePoem(Poem poem, String id, UserDetails userDetails) {

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        Poem previousPoem = poemRepository.findById(id).orElse(null);

        // ? Only the user and an admin can update the poem
        if(!user.equals(previousPoem.getAuthor()) && user.getRole() != Role.ADMIN) {
            responseDTO.setCode(VarList.RSP_NOT_AUTHORISED);
            responseDTO.setMessage("No permission to update");
            responseDTO.setContent(null);
            return responseDTO;
        }

        poem.setId(id);
        poem.setClicks(previousPoem.getClicks());
        if(poem.getTitle() == null) poem.setTitle(previousPoem.getTitle());
        if(poem.getContent() == null) poem.setContent(previousPoem.getContent());

        poem.setViews(previousPoem.getViews());
        poem.setVisibility(previousPoem.getVisibility());
        poem.setUpvotes(previousPoem.getUpvotes());
        poem.setDownvotes(previousPoem.getDownvotes());
        poem.setAuthor(previousPoem.getAuthor());

        Poem updatedPoem = poemRepository.save(poem);

        if(updatedPoem == null){
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setContent(null);
            responseDTO.setMessage("Poem update fail");
        }else{
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setContent(updatedPoem);
            responseDTO.setMessage("Poem updated successfully");
        }

        return responseDTO;
    }

    public ResponseDTO updateUpvote(String id, UserDetails userDetails, boolean increment){

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        Poem poem = poemRepository.findById(id).orElse(null);

        if(poem == null ){
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Poem not found");
        } else {
            Integer upvotes = poem.getUpvotes();

            if(upvotes == null) upvotes = 0;

            if(increment) upvotes += 1;
            else upvotes -= 1;

            poem.setUpvotes(upvotes);

            Poem updatedPoem = poemRepository.save(poem);

            if(updatedPoem == null){
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setContent(null);
                responseDTO.setMessage("Upvote update fail");
            }else{

                VoteRecords upvoteRecord = new VoteRecords();
                upvoteRecord.setPoem(poem);
                upvoteRecord.setUser(user);
                upvoteRecord.setDatetime(new Date());
                upvoteRecord.setIsupvote(true);

                voteRecordRepository.save(upvoteRecord);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(updatedPoem);
                responseDTO.setMessage("Upvote updated successfully");
            }
        }

        return responseDTO;
    }


    public ResponseDTO updateDownvote(String id, UserDetails userDetails, boolean increment){

        AuthUser user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        Poem poem = poemRepository.findById(id).orElse(null);

        if(poem == null ){
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Poem not found");
        } else {
            Integer downvotes = poem.getDownvotes();

            if(downvotes == null) downvotes = 0;

            if(increment) downvotes += 1;
            else downvotes -= 1;

            poem.setUpvotes(downvotes);

            Poem updatedPoem = poemRepository.save(poem);

            if(updatedPoem == null){
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setContent(null);
                responseDTO.setMessage("Downvote update fail");
            }else{

                VoteRecords downvoteRecord = new VoteRecords();
                downvoteRecord.setPoem(poem);
                downvoteRecord.setUser(user);
                downvoteRecord.setDatetime(new Date());
                downvoteRecord.setIsupvote(false);

                voteRecordRepository.save(downvoteRecord);

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(updatedPoem);
                responseDTO.setMessage("Downvote updated successfully");
            }
        }

        return responseDTO;
    }
}

