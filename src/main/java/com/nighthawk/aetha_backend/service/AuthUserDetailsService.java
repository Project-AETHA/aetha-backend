package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {


    @Autowired
    public AuthUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AuthUser> authUser = userRepository.findByEmail(username.toLowerCase());

        if (authUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else {

            // Granting authority based on the user's role
            GrantedAuthority authority = new SimpleGrantedAuthority
                    ("ROLE_" + authUser.get().getRole().name());

            return User.builder()
                    .username(authUser.get().getUsername())
                    .password(authUser.get().getPassword())
                    .authorities((Collections.singletonList(authority)))
                    .build();
        }
    }
}
