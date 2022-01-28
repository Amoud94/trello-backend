package com.trello.backend.demo;

import com.trello.backend.demo.entity.ERole;
import com.trello.backend.demo.entity.Role;
import com.trello.backend.demo.service.AuthenticationServices;
import com.trello.backend.demo.service.FilesUploadStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class TrelloCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrelloCloneApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AuthenticationServices authenticationServices, FilesUploadStorageService storageService){
        return args->{
            authenticationServices.save(new Role(ERole.ROLE_USER));
            authenticationServices.save(new Role(ERole.ROLE_ADMIN));

            storageService.deleteAll();
            storageService.init();
        };
    }
}
