package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.StatDTO;
import com.nighthawk.aetha_backend.dto.UserDTO;
import com.nighthawk.aetha_backend.entity.AccStatus;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Role;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.SupportTicketRepository;
import com.nighthawk.aetha_backend.utils.StatusList;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthUserRepository repository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO getAllUsers() {

        List<AuthUser> users = repository.findAll();

        try {
            if (!users.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Listing all users");

                List<UserDTO> authUsers = users.stream()
                        .map(authUser -> modelMapper.map(authUser, UserDTO.class))
                        .collect(Collectors.toList());

                responseDTO.setContent(authUsers);

            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No users found");
                responseDTO.setContent(null);

            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO findByEmail(String email) {
        AuthUser user = repository.findByEmail(email).orElse(null);

        try {
            if (user == null) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
            } else {
                responseDTO.setMessage("User found");
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(user);
            }
        } catch (Exception e) {

            responseDTO.setMessage(e.getMessage());
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public ResponseDTO updateUser(String email, AuthUser newUser) {
        AuthUser user = repository.findByEmail(email).orElse(null);

        if (user != null) {
            if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
                user.setEmail(newUser.getEmail());
            }
            if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            }
            if (newUser.getFirstname() != null && !newUser.getFirstname().isEmpty()) {
                user.setFirstname(newUser.getFirstname());
            }
            if (newUser.getLastname() != null && !newUser.getLastname().isEmpty()) {
                user.setLastname(newUser.getLastname());
            }
            if (newUser.getGender() != null && !newUser.getGender().isEmpty()) {
                user.setGender(newUser.getGender());
            }
            if (newUser.getBirthdate() != null) {
                user.setBirthdate(newUser.getBirthdate());
            }
            if (newUser.getRole() != null) {
                user.setRole(newUser.getRole());
            }

            // Update the display name with proper capitalization
            user.setDisplayName(capitalizeWords(user.getFirstname() + " " + user.getLastname()));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User updated successfully");
            responseDTO.setContent(repository.save(user));
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("User not found to Update");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    private String capitalizeWords(String str) {
        String[] words = str.split("\\s+");
        StringBuilder capitalizedStr = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedStr.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalizedStr.toString().trim();
    }

    public ResponseDTO deleteUser(String email) {

        try {
            AuthUser user = repository.findByEmail(email).orElse(null);

            if (user != null) {
                user.setStatus(AccStatus.DELETED);
                AuthUser deletedUser = repository.save(user);

                if (deletedUser != null) {
                    responseDTO.setCode(VarList.RSP_SUCCESS);
                    responseDTO.setMessage("User deleted Successfully");
                    responseDTO.setContent(deletedUser);

                } else {
                    responseDTO.setCode(VarList.RSP_FAIL);
                    responseDTO.setMessage("Failed to Delete");
                    responseDTO.setContent(null);

                }
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User Not Found");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {

            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO addUser(AuthUser user) {

        try {
            HashMap<String, String> errors = new HashMap<>();

            if (repository.findByEmail(user.getEmail()).isPresent()) {
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(null);
            } else {

                if (user.getFirstname() == null) {
                    errors.put("Firstname", "First name cannot be empty");
                }
                if (user.getLastname() == null) {
                    errors.put("Lastname", "Last name cannot be empty");
                }
                if (user.getEmail() == null) {
                    errors.put("Email", "Email cannot be empty");
                }
                if (user.getPassword() == null) {
                    errors.put("Password", "Password cannot be empty");
                }

                if (errors.isEmpty()) {
                    user.setDisplayName(user.getFirstname().concat(" ").concat(user.getLastname()));
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setStatus(AccStatus.ACTIVE);

                    AuthUser newuser = repository.save(user);

                    if (newuser != null) {

                        responseDTO.setCode(VarList.RSP_SUCCESS);
                        responseDTO.setMessage("User added Successfully");
                        responseDTO.setContent(newuser);
                    } else {

                        responseDTO.setCode(VarList.RSP_ERROR);
                        responseDTO.setMessage("Failed user adding");
                        responseDTO.setContent(null);
                    }
                }
            }

        } catch (Exception e) {

            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO disableUser ( String email){

          try {
              AuthUser user = repository.findByEmail(email).orElse(null);

              if(user != null) {
                   user.setStatus(AccStatus.DISABLED);
                   AuthUser disabledUser = repository.save(user);

                   responseDTO.setCode(VarList.RSP_SUCCESS);
                   responseDTO.setMessage("User disabled Successfully");
                   responseDTO.setContent(disabledUser);

              } else {
                  responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                  responseDTO.setMessage("User not found");
                  responseDTO.setContent(null);

              }
          } catch (Exception e) {

              responseDTO.setCode(VarList.RSP_FAIL);
              responseDTO.setMessage(e.getMessage());
              responseDTO.setContent(null);
          }

          return responseDTO;
    }

    public ResponseDTO getMyDetails(UserDetails userDetails) {
        try {
            AuthUser user = repository.findByEmail(userDetails.getUsername()).orElse(null);

            if (user != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User found");
                    responseDTO.setContent(user);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO upgradeUser(UserDetails userDetails) {
        try {
            AuthUser user = repository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

            if(user.getRole().equals(Role.READER)) {
                user.setRole(Role.WRITER);
            } else if(user.getRole().equals(Role.ADMIN)) {
                throw new Exception("Admin cannot be upgraded");
            }

            AuthUser upgradedUser = repository.save(user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User upgraded Successfully");
            responseDTO.setContent(upgradedUser);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }
}

