package com.example.demo.util;

import java.util.ArrayList;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.domain.dto.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({ IdException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class })
    public ResponseEntity<RestResponse<String>> solveException(Exception e) {
        RestResponse<String> rest = new RestResponse<>();
        rest.setStatus(400);
        rest.setMess(e.getMessage());
        rest.setError("Error orrcured");
        rest.setData(null);
        return ResponseEntity.badRequest().body(rest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<String>> sovleInvalid(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }

        RestResponse<String> rest = new RestResponse<>();
        rest.setStatus(400);
        rest.setMess(errors.toString());
        rest.setError("Validation Error");
        rest.setData(null);
        return ResponseEntity.badRequest().body(rest);
    }
}
