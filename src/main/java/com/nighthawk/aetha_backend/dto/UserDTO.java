package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private String username;
    private String displayName;
    private String email;
    private String firstname;
    private String lastname;
    private String image;
    private String gender;
    private Date birthdate;
    private Role role;

}
