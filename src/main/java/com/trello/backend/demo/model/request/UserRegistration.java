package com.trello.backend.demo.model.request;

import com.trello.backend.demo.entity.Board;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserRegistration {
    @NotNull(message = "firstname cannot be null")
    private String firstname;
    @NotNull(message = "lastname cannot be null")
    private String lastname;
    @NotNull(message = "username cannot be null")
    private String username;
    @NotNull(message = "password cannot be null")
    @Size(min = 3,max = 10,message = "password must be equal or grater than 3 and less than 10")
    private String password;
    @NotNull(message = "email cannot be null")
    @Email
    private String email;
    @NotNull(message = "phone number cannot be null")
    private String phoneNumber;
    private String role;
    private Set<Board> boards = new HashSet<>();
}
