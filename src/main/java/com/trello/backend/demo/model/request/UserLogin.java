package com.trello.backend.demo.model.request;

import lombok.Data;

@Data
public class UserLogin {
    private String username;
    private String password;
}
