package com.trello.backend.demo.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String userID;
    private String username;
    private Boolean loggedIn;
}
