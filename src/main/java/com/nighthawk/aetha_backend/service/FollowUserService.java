package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.FollowUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.FollowUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FollowUserService {

    private final FollowUserRepository followUserRepository;
    private final AuthUserRepository authUserRepository;
    private ResponseDTO responseDTO;

    @Autowired
    public FollowUserService(FollowUserRepository followUserRepository, ResponseDTO responseDTO, AuthUserRepository authUserRepository) {
        this.followUserRepository = followUserRepository;
        this.responseDTO = responseDTO;
        this.authUserRepository = authUserRepository;
    }


    //? Follow a user
    public ResponseDTO followAUser(UserDetails userDetails, String followingUserId) {

        try {
            //? Get the user that is following
            AuthUser follower = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found - Follower"));
            AuthUser following = authUserRepository.findById(followingUserId).orElseThrow(() -> new NoSuchElementException("User not found - Following"));

            if(follower.getId().equals(following.getId())) throw new IllegalArgumentException("You cannot follow yourself");

            if(followUserRepository.findByFollowerAndFollowing(follower, following).isPresent()) throw new IllegalArgumentException("You are already following this user");

            FollowUser followUser = new FollowUser();
            followUser.setFollower(follower);
            followUser.setFollowing(following);
            followUser.setFollowedAt(LocalDate.now(ZoneId.systemDefault()));

            followUserRepository.save(followUser);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User followed successfully");
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

    //? Unfollow a user
    public ResponseDTO unfollowAUser(UserDetails userDetails, String followingId) {

        try {
            AuthUser follower = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found - Follower"));
            AuthUser following = authUserRepository.findById(followingId).orElseThrow(() -> new NoSuchElementException("User not found - Following"));

            FollowUser currentFollowingRecord = followUserRepository.findByFollowerAndFollowing(follower, following).orElseThrow(() -> new NoSuchElementException("No follow record found"));

            followUserRepository.delete(currentFollowingRecord);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User unfollowed successfully");

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

    //? Get the following list of currently logged-in user
    public ResponseDTO getFollowingList(UserDetails userDetails) {

        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            List<FollowUser> followingList = followUserRepository.findByFollower(user);
            List<AuthUser> followingUsers = followingList.stream().map(FollowUser::getFollowing).toList();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Following list retrieved successfully");
            responseDTO.setContent(followingUsers);

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

    //? Get the followers list of currently logged-in user
    public ResponseDTO getFollowersList(UserDetails userDetails) {

            try {
                AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

                List<FollowUser> followersList = followUserRepository.findByFollowing(user);
                List<AuthUser> followingUsers = followersList.stream().map(FollowUser::getFollower).toList();

                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Followers list retrieved successfully");
                responseDTO.setContent(followingUsers);

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

    //? Get Followers and Following count
    public ResponseDTO getFollowersAndFollowingCount(UserDetails userDetails) {
        try {
            AuthUser user = authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            HashMap<String, Long> count = new HashMap<>();
            count.put("followers", followUserRepository.countByFollowing(user));
            count.put("following", followUserRepository.countByFollower(user));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Followers and Following count retrieved successfully");
            responseDTO.setContent(count);

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
