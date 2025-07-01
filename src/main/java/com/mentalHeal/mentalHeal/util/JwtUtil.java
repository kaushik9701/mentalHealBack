package com.mentalHeal.mentalHeal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // Move to application.properties
    private String secret;

    @Value("${jwt.expiration}") // In milliseconds
    private long expirationTime;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuer("mentalHealApp")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(getAlgorithm());
    }

    public String validateTokenAndGetEmail(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm())
                    .withIssuer("mentalHealApp")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            return null; // Token is invalid
        }
    }
}