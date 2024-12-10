package com.example.demo.util;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({ IdException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class })
    public ResponseEntity<String> solveException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
