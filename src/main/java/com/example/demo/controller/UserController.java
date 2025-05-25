package com.example.demo.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.StringDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.IdException;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate<String,Object> template;
    @PostMapping("/users")
    @ApiMessage(value = "Create User")
    public ResponseEntity<StringDto> postCreateUser(@Valid @RequestBody User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        Role role = roleService.fetchById(user.getRole().getId());
        user.setRole(role);
        userService.handleCreateUser(user);
        StringDto stringDto = new StringDto();
        stringDto.setResult("create success");
        return ResponseEntity.ok().body(stringDto);
    }

    @GetMapping("/users")
    @ApiMessage(value = "Fetch All Users")
    public ResponseEntity<List<UserDto>> getFetchAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        List<UserDto> users = userService.fetchAllUsersNoPage();
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
        Role role = roleService.fetchById(user.getRole().getId());
        user.setRole(role);
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
    @GetMapping("/clear-cache")
    public ResponseEntity<StringDto> deleteCache(){
        template.delete("users");
        StringDto stringDto = new StringDto();
        stringDto.setResult("xóa cache thành công");
        return ResponseEntity.ok().body(stringDto);
    }
}
