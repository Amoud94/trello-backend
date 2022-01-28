package com.trello.backend.demo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.model.response.LoginResponse;
import com.trello.backend.demo.utils.SecurityParams;
import com.trello.backend.demo.service.AuthenticationServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    AuthenticationServices authenticationServices;

    @Autowired
    public JWTAuthenticationFilter(AuthenticationServices authenticationServices ,
                                   AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationServices = authenticationServices;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            AppUser appUsr = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            appUsr.getUsername(),appUsr.getPassword(),new ArrayList<>()));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        LoginResponse loginResponse = new LoginResponse();

        String  username = ((User) authResult.getPrincipal()).getUsername();
        User s = ((User) authResult.getPrincipal());
        s.getAuthorities();

        AppUser appUser = authenticationServices.loadUserByUsername(username);
        String access_token= Jwts.builder()
                .setSubject(appUser.getAppUserID())
                .claim("username", appUser.getUsername())
                .claim("granted authorities", appUser.getRoles())
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityParams.EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SecurityParams.SECRET)
                .compact();
        response.addHeader("Authorization",access_token);

        loginResponse.setUserID(appUser.getAppUserID());
        loginResponse.setUsername(appUser.getUsername());
        loginResponse.setToken(access_token);
        loginResponse.setLoggedIn(true);
        String json = new ObjectMapper().writeValueAsString(loginResponse);

        response.getWriter().write(json);
        response.flushBuffer();
    }
}
