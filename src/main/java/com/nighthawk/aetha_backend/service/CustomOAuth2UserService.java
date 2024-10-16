package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private AuthUserRepository authUserRepository;

    // Delegate the default OAuth2UserService to load user info
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);  // Delegate to load the user

        String email = oAuth2User.getAttribute("email");

        // Find or create user in your local repository
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseGet(() -> createUser(oAuth2User));

        // Return the authenticated user with attributes
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(oAuth2User.getAttributes())),
                oAuth2User.getAttributes(),
                "email");
    }

    private AuthUser createUser(OAuth2User oAuth2User) {
        AuthUser user = new AuthUser();
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setFirstname(oAuth2User.getAttribute("name"));
        user.setUsername(oAuth2User.getAttribute("email"));

        authUserRepository.save(user);
        return user;
    }
}