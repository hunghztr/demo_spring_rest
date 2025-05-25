package com.example.demo.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.SecurityUtil;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RedisTemplate<String,Object> template;

    private Gson gson = new Gson();

    public boolean isExistByUsername(String username) {
        return userRepository.existsByName(username);
    }

    public User handleCreateUser(User user) {
        return userRepository.save(user);
    }

    public List<UserDto> fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pages = userRepository.findAll(spec, pageable);
        List<User> users = pages.getContent();

        List<UserDto> userDtos = users.stream().map(u -> userMapper.toUserDto(u)).collect(Collectors.toList());
        return userDtos;
    }

    public User handleUpdateUser(User user, long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User curUser = optional.get();
            curUser.setName(user.getName());
            curUser.setPassword(user.getPassword());
            curUser.setRole(user.getRole());
            return userRepository.save(curUser);
        }
        return null;
    }

    public User handleDeleteUser(long id) {
        return userRepository.deleteById(id);
    }

    public UserDto fetchUserById(long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User curUser = optional.get();
            return userMapper.toUserDto(curUser);
        }
        return null;
    }
    public List<UserDto> fetchAllUsersNoPage(){
        String jsonStr = (String)template.opsForValue().get("users");
        if(jsonStr == null) {
            log.info("get data from mysql");
            List<User> users = userRepository.findAll();
            List<UserDto> userDtos = users.stream().map(u -> userMapper.toUserDto(u)).collect(Collectors.toList());
            String json = gson.toJson(userDtos);
            template.opsForValue().set("users", json);
            return userDtos;
        }else{
            log.info("get data from redis");
            Type type = new TypeToken<List<UserDto>>(){}.getType();
            List<UserDto> userDtos = gson.fromJson(jsonStr, type);
            return userDtos;
        }
    }

    public User fetchUserByName(String name) {
        Optional<User> optional = userRepository.findByName(name);
        if (optional.isPresent()) {
            User user = optional.get();
            return user;
        }
        return null;
    }

    public long countUser() {
        return userRepository.count();
    }

    public User updateToken(String name) {
        User curUser = fetchUserByName(name);
        String refreshToken = securityUtil.generateRefreshToken(curUser);
        curUser.setToken(refreshToken);
        curUser = handleUpdateUser(curUser, curUser.getId());
        return curUser;
    }

    public ResponseCookie createCookie(String token, int maxAge) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
