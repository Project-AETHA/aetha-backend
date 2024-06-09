package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        repository.save(user);

        return VarList.RSP_SUCCESS;
    }

}
