package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

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

    public User fetchUserByName(String name) {
        Optional<User> optional = userRepository.findByName(name);
        if (optional.isPresent()) {
            User user = optional.get();
            return user;
        }
        return null;
    }
}
