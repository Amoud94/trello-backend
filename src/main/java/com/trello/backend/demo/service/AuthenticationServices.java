package com.trello.backend.demo.service;

import com.trello.backend.demo.dto.AppUserDTO;
import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.entity.Role;

public interface AuthenticationServices {

     AppUser saveUser(AppUserDTO appUserDTO) throws Exception;
     Role save(Role role);
     AppUser loadUserByUsername(String username);
}
