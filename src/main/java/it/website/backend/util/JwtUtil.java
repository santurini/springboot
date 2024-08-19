package it.website.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey; // Ensure this is securely managed

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.temporary_expiration}")
    private long temporary_expiration;

    // Generate a JWT token
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateTemporaryToken(String sessionId) {
        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + temporary_expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract claims from the JWT token
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    // Extract the subject from the JWT token
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if the JWT token is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Validate the JWT token
    public boolean validateToken(String token, String subject) {
        return (subject.equals(extractSubject(token)) && !isTokenExpired(token));
    }

    public boolean isTemporaryToken(String jwt) {
        Claims claims = extractClaims(jwt);
        return claims.getExpiration().before(new Date(System.currentTimeMillis() + temporary_expiration));
    }
}
