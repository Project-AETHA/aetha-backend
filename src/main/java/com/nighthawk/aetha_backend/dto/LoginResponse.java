package com.nighthawk.aetha_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String jwtToken;
    private String username;
    private List<String> roles;
    private UserDTO user;

    public LoginResponse(String username, List<String> roles, String jwtToken, UserDTO user) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.user = user;
    }

}
