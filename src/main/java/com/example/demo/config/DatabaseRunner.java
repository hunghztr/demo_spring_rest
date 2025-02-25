package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;

import com.example.demo.domain.User;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;

@Component
public class DatabaseRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (permissionService.countPermission() == 0) {
            // tạo quyền thêm
            Permission createUser = new Permission();
            createUser.setName("Create User");
            createUser.setApi("/api/v1/users");
            createUser.setModule("User");
            createUser.setMethod("POST");
            permissionService.create(createUser);
            // tạo quyền sửa
            Permission updateUser = new Permission();
            updateUser.setName("Update User");
            updateUser.setApi("/api/v1/users/{id}");
            updateUser.setModule("User");
            updateUser.setMethod("PUT");
            permissionService.create(updateUser);
            // tạo quyền xóa
            Permission deleteUser = new Permission();
            deleteUser.setName("Delete User");
            deleteUser.setApi("/api/v1/users/{id}");
            deleteUser.setModule("User");
            deleteUser.setMethod("DELETE");
            permissionService.create(deleteUser);
            // tạo quyền xem tất cả
            Permission fetchAllUsers = new Permission();
            fetchAllUsers.setName("Fetch All Users");
            fetchAllUsers.setApi("/api/v1/users");
            fetchAllUsers.setModule("User");
            fetchAllUsers.setMethod("GET");
            permissionService.create(fetchAllUsers);
            // tạo quyền xem theo id
            Permission fetchUserById = new Permission();
            fetchUserById.setName("Fetch User By Id");
            fetchUserById.setApi("/api/v1/users/{id}");
            fetchUserById.setModule("User");
            fetchUserById.setMethod("GET");
            permissionService.create(fetchUserById);
        }
        if (roleService.countRole() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleAdmin.setPermissions(permissionService.getAll());
            roleService.create(roleAdmin);
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleService.create(roleUser);
        }

        if (userService.countUser() == 0) {
            User admin = new User();
            admin.setName("admin@gmail.com");
            String password = passwordEncoder.encode("123");
            admin.setPassword(password);
            admin.setRole(roleService.getRoleByName("ROLE_ADMIN"));
            userService.handleCreateUser(admin);
        }

    }

}
