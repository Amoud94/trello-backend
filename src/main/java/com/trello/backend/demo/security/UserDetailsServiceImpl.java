package com.trello.backend.demo.security;

import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.service.AuthenticationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthenticationServices authenticationServices;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = authenticationServices.loadUserByUsername(username);
        if(appUser == null) throw new UsernameNotFoundException("invalid user credentials");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
        });
        return new User(appUser.getUsername(), appUser.getPassword(),
                true,true,true,
                true,authorities);
    }
}
