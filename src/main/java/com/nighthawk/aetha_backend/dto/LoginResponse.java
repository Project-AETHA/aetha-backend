package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String jwtToken;
    private String username;
    private List<String> roles;
    private AuthUser user;

    public LoginResponse(String username, List<String> roles, String jwtToken, AuthUser user) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.user = user;
    }

}
