package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,String> {
    AppUser findByAppUserID(String appUserID);
    AppUser findByUsername(String username);
}
