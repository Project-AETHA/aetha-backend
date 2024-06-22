package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AuthUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResponseDTO responseDTO;

    public List<AuthUser> getAllUsers() {
        return repository.findAll();
    }

    public AuthUser findByEmail(String email) {

        Optional<AuthUser> user = repository.findByEmail(email);

        return user.orElse(null);
    }

    public AuthUser updateUser(String email, AuthUser newUser) {
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

            return repository.save(user);
        }

        return null;
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

    public AuthUser deleteUser(String email) {

        AuthUser user = repository.findByEmail(email).orElse(null);

        if(user != null) {
            user.setDeleted(true);
            repository.save(user);
            return user;
        } else {
            return null;
        }
    }
}
