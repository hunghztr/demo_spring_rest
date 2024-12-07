package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.util.IdException;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<String> postCreateUser(@RequestBody User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userService.handleCreateUser(user);
        return ResponseEntity.ok().body("create success");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getFetchAllUsers() {
        List<User> users = userService.fetchAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) throws IdException {
        User user = userService.fetchUserById(id);
        if (user == null) {
            throw new IdException("người dùng không tồn tại");
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> putUpdateUser(@PathVariable("id") long id, @RequestBody User user) {
        userService.handleUpdateUser(user, id);
        return ResponseEntity.ok().body("update success");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteRemoveUser(@PathVariable("id") long id) {
        userService.handleDeleteUser(id);
        return ResponseEntity.ok().body("delete success");
    }

}
