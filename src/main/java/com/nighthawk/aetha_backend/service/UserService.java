package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Role;
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

    public String registerUser(AuthUser user) {

        if(repository.findByUsername(user.getUsername()).isPresent()) {
            return VarList.RSP_DUPLICATED;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.READER);

        repository.save(user);

        return VarList.RSP_SUCCESS;
    }

    public List<AuthUser> getAllUsers() {
        return repository.findAll();
    }

    public Optional<AuthUser> findUserByUsername(String username) {
        return repository.findByUsername(username);
    }
}
