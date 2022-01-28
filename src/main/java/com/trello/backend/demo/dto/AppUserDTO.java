package com.trello.backend.demo.dto;

import com.trello.backend.demo.entity.Board;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class AppUserDTO {
    private String appUserID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Boolean activated;
    private String role;
}
