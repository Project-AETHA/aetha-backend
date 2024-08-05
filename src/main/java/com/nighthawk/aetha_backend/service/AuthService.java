package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.LoginRequest;
import com.nighthawk.aetha_backend.dto.LoginResponse;
import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.UserDTO;
import com.nighthawk.aetha_backend.entity.AccStatus;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Role;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.Jwtutils;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Jwtutils jwtutils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private AuthUserRepository userRepository;

    public ResponseDTO login(LoginRequest loginRequest) {

        AuthUser user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

        if(user != null && user.getStatus() == AccStatus.DISABLED) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User is disabled");
            responseDTO.setContent(null);

            return responseDTO;
        } else if(user != null && user.getStatus() == AccStatus.DELETED) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("User was deleted");
            responseDTO.setContent(null);

            return responseDTO;
        }

        try {

            Authentication authentication;

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtutils.generateTokenFromUsername(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            ModelMapper modelMapper = new ModelMapper();

            LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken, modelMapper.map(user, UserDTO.class));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Login Successful");
            responseDTO.setContent(response);

        } catch (AuthenticationException e) {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Bad Credentials");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }



    public ResponseDTO register (AuthUser user) {
        try {
            if(userRepository.findByEmail(user.getUsername()).isPresent()) {
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Email already exists");

                throw new Exception("Email already exists");
            }

            user.setUsername(user.getEmail());
            user.setDisplayName(user.getFirstname().concat(" ").concat(user.getLastname()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(
                    user.getRole().equals(Role.ADMIN)
                            ? Role.ADMIN
                            : user.getRole().equals(Role.WRITER)
                                ? Role.WRITER
                                : Role.READER
            );
            user.setStatus(AccStatus.ACTIVE);

            userRepository.save(user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User registered successfully");
            responseDTO.setContent(user);

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

}
