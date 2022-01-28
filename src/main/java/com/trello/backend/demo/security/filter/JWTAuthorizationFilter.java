package com.trello.backend.demo.security.filter;

import com.trello.backend.demo.utils.SecurityParams;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class JWTAuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURI().equals("/api/login") || request.getRequestURI().equals("/api/logout") || request.getRequestURI().equals("/api/register")) {
                filterChain.doFilter(request, response);
                return;
            } else {
                String authorizationHeader = request.getHeader(SecurityParams.JWT_HEADER_NAME);
                System.out.println("authorizationHeader = " + authorizationHeader);
                if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityParams.HEADER_PREFIX)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (ServletException e) {
            throw new ServletException(e);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityParams.JWT_HEADER_NAME);
        if (authorizationHeader == null) return null;
        String token = authorizationHeader.replace(SecurityParams.HEADER_PREFIX, "");
        String userID = Jwts.parser()
                .setSigningKey(SecurityParams.SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if (userID == null) return null;
        return new UsernamePasswordAuthenticationToken(userID, null, new ArrayList<>());
    }
}
