package org.example.jobservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }

    public Long extractUserId(String token) {
        Object userId = Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }
}
