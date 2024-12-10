package com.example.demo.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;

import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;

import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class SecurityUtil {

    @Value("${jwt.key}")
    public String KEY;
    @Value("${jwt.time}")
    private long expiration;

    public String generateToken(Authentication authentication) {

        var key = Base64.getDecoder().decode(KEY);
        Instant now = Instant.now();
        Instant expir = now.plus(expiration, ChronoUnit.SECONDS);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(expir.toEpochMilli()))
                .subject(authentication.getName())
                .claim("user", authentication)
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

}
