package com.example.demo.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.service.UserDetailsCustom;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class SecurityUtil {
    private final ApplicationContext context;
    @Value("${jwt.key}")
    public String KEY;
    @Value("${jwt.time}")
    private long expiration;

    public SecurityUtil(ApplicationContext context) {
        this.context = context;
    }

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

    public boolean validToken(String token) {
        boolean isValid = false;
        var key = Base64.getDecoder().decode(KEY);
        try {
            JWSVerifier verifier = new MACVerifier(key);
            SignedJWT sign = SignedJWT.parse(token);
            isValid = sign.verify(verifier);
            Date expir = sign.getJWTClaimsSet().getExpirationTime();
            String username = sign.getJWTClaimsSet().getSubject();
            UserDetails userDetails = context.getBean(UserDetailsCustom.class)
                    .loadUserByUsername(username);
            if (!expir.after(new Date())) {
                isValid = false;
            }
            if (isValid && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            e.printStackTrace();
            isValid = false;
        }
        return isValid;
    }

}
