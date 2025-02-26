package com.example.demo.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;

import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;

import com.nimbusds.jwt.JWTClaimsSet;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityUtil {

    @Value("${jwt.key}")
    public String KEY;
    @Value("${jwt.time}")
    private long expiration;
    @Value("${jwt.time-of-refresh}")
    private long expirationRefresh;

    public String generateAccessToken(User auth) {
        Role role = auth.getRole();
        var key = Base64.getDecoder().decode(KEY);
        Instant now = Instant.now();
        Instant expir = now.plus(expiration, ChronoUnit.SECONDS);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(expir.toEpochMilli()))
                .subject(auth.getName())
                .claim("role", role.getName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject object = new JWSObject(header, payload);
        try {
            object.sign(new MACSigner(key));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object.serialize();
    }

    public String generateRefreshToken(User auth) {
        var key = Base64.getDecoder().decode(KEY);
        Instant now = Instant.now();
        Instant expir = now.plus(expirationRefresh, ChronoUnit.SECONDS);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(expir.toEpochMilli()))
                .subject(auth.getName())
                .claim("user", auth.getName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject object = new JWSObject(header, payload);
        try {
            object.sign(new MACSigner(key));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object.serialize();
    }

    public static String getUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auth.getAuthorities().forEach(g -> log.info("role >>> " + g.getAuthority()));
            return auth.getName();
        }
        return null;
    }
}
