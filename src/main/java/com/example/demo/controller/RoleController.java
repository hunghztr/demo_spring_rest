package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/v1")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @PutMapping("/roles")
    public ResponseEntity<Role> addPermission(@RequestBody Role entity) {

        List<Long> ids = entity.getPermissions().stream().map(p -> p.getId()).collect(Collectors.toList());
        List<Permission> permissions = permissionService.fetchAllByIds(ids);
        entity.setPermissions(permissions);
        Role role = roleService.fetchById(entity.getId());
        entity.setName(role.getName());
        entity = roleService.create(entity);
        return ResponseEntity.ok().body(entity);
    }
}
