package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.util.IdException;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> postCreateUser(@RequestBody User user) {
        User curUser = userService.handleCreateUser(user);
        return ResponseEntity.ok().body(curUser);
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
    public ResponseEntity<User> putUpdateUser(@PathVariable("id") long id, @RequestBody User user) {
        User curUser = userService.handleUpdateUser(user, id);
        return ResponseEntity.ok().body(curUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteRemoveUser(@PathVariable("id") long id) {
        User curUser = userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(curUser);
    }

}
