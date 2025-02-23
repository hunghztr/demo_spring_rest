package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.StringDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.util.IdException;
import com.example.demo.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage(value = "Create User")
    public ResponseEntity<StringDto> postCreateUser(@Valid @RequestBody User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userService.handleCreateUser(user);
        StringDto stringDto = new StringDto();
        stringDto.setResult("create success");
        return ResponseEntity.ok().body(stringDto);
    }

    @GetMapping("/users")
    @ApiMessage(value = "Fetch All Users")
    public ResponseEntity<List<UserDto>> getFetchAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        List<UserDto> users = userService.fetchAllUsers(spec, pageable);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/users/{id}")
    @ApiMessage(value = "Fetch User By Id")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) throws IdException {
        UserDto user = userService.fetchUserById(id);
        if (user == null) {
            throw new IdException("người dùng không tồn tại");
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/users/{id}")
    @ApiMessage(value = "Update User")
    public ResponseEntity<StringDto> putUpdateUser(@PathVariable("id") long id, @RequestBody User user) {
        userService.handleUpdateUser(user, id);
        StringDto stringDto = new StringDto();
        stringDto.setResult("update success");
        return ResponseEntity.ok().body(stringDto);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage(value = "Delete User")
    public ResponseEntity<StringDto> deleteRemoveUser(@PathVariable("id") long id) {
        userService.handleDeleteUser(id);
        StringDto stringDto = new StringDto();
        stringDto.setResult("delete success");
        return ResponseEntity.ok().body(stringDto);
    }

}
