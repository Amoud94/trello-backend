package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.ERole;
import com.trello.backend.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByName(ERole name);
}
