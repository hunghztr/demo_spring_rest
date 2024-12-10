package com.example.demo.util;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidator extends OncePerRequestFilter {

    private final SecurityUtil securityUtil;

    public JwtValidator(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            boolean isValid = securityUtil.validToken(token);
            if (isValid) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(403);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
