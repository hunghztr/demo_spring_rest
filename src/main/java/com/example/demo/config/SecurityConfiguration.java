package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.service.UserDetailsCustom;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtValidator;
import com.example.demo.util.SecurityUtil;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    public SecurityConfiguration(UserService userService, SecurityUtil securityUtil) {
        this.userService = userService;
        this.securityUtil = securityUtil;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsCustom(userService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtValidator(securityUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .formLogin(f -> f.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

}
