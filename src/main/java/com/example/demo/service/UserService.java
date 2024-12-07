package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
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

    public User fetchUserById(long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User curUser = optional.get();
            return curUser;
        }
        return null;
    }
}
