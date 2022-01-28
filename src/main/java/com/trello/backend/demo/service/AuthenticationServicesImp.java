package com.trello.backend.demo.service;

import com.trello.backend.demo.dto.AppUserDTO;
import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.entity.ERole;
import com.trello.backend.demo.entity.Role;
import com.trello.backend.demo.repo.AppUserRepository;
import com.trello.backend.demo.repo.RoleRepository;
import com.trello.backend.demo.service.AuthenticationServices;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationServicesImp(AppUserRepository appUserRepository,
                                     RoleRepository roleRepository,
                                     BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AppUser saveUser(AppUserDTO appUserDTO) throws Exception {
        try {
            AppUser appUsr = appUserRepository.findByUsername(appUserDTO.getUsername());
            if (appUsr != null) throw new RuntimeException("User already exists");
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            appUsr = mapper.map(appUserDTO, AppUser.class);
            appUsr.setAppUserID(UUID.randomUUID().toString());
            appUsr.setActivated(true);
            appUsr.setPassword(bCryptPasswordEncoder.encode(appUserDTO.getPassword()));
            Role claims = new Role();
            if (appUserDTO.getRole().equals("admin")) {
                claims = roleRepository.findByName(ERole.ROLE_ADMIN);
            } else if (appUserDTO.getRole().equals("user")) {
                claims = roleRepository.findByName(ERole.ROLE_USER);
            }
            appUsr.getRoles().add(claims);
            AppUser user = appUserRepository.save(appUsr);
            return user;
        }
        catch( Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

}
