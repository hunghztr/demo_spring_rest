package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.StringDto;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.IdException;
import com.example.demo.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @PostMapping("/auth/login")
    public ResponseEntity<StringDto> postLogin(@RequestBody User user) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getName(), user.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            StringDto stringDto = new StringDto();
            User curUser = userService.updateToken(user.getName());
            String accessToken = securityUtil.generateAccessToken(curUser);
            ResponseCookie cookie = userService.createCookie(curUser.getToken(), 3600);
            stringDto.setResult(accessToken);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(stringDto);
        } catch (Exception e) {
            throw new BadCredentialsException("username or password is incorrected ...");
        }
    }

    @GetMapping("/auth/get-info")
    public ResponseEntity<StringDto> getCurrent() {
        StringDto stringDto = new StringDto();
        stringDto.setResult(SecurityUtil.getUsername());
        return ResponseEntity.ok().body(stringDto);
    }

    @GetMapping("/auth/get-token")
    public ResponseEntity<StringDto> getToken(
            @CookieValue(name = "refresh_token", defaultValue = "no_token") String token) {
        if (token.equals("no_token")) {
            StringDto stringDto = new StringDto();
            stringDto.setResult("no token");
            return ResponseEntity.badRequest().body(stringDto);
        }
        String username = SecurityUtil.getUsername();
        StringDto stringDto = new StringDto();
        User curUser = userService.updateToken(username);
        String accessToken = securityUtil.generateAccessToken(curUser);
        ResponseCookie cookie = userService.createCookie(curUser.getToken(), 3600);
        stringDto.setResult(accessToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(stringDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<StringDto> logout() {
        ResponseCookie cookie = userService.createCookie(null, 0);
        String username = SecurityUtil.getUsername();
        User user = userService.fetchUserByName(username);
        if (user != null) {
            user.setToken("");
            userService.handleUpdateUser(user, user.getId());
        }
        StringDto stringDto = new StringDto();
        stringDto.setResult("logout success");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(stringDto);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<StringDto> register(@RequestBody User entity) throws IdException {
        if (userService.isExistByUsername(entity.getName())) {
            throw new IdException("username is existed ...");
        }
        String password = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(password);
        if (entity.getRole() != null) {

            Role role = roleService.fetchById(entity.getRole().getId());
            entity.setRole(role);
        }
        userService.handleCreateUser(entity);
        StringDto stringDto = new StringDto();
        stringDto.setResult("register success");
        return ResponseEntity.ok().body(stringDto);
    }

}
