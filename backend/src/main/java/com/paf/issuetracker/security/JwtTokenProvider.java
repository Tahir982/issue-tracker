package com.paf.issuetracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")   private String jwtSecret;
    @Value("${app.jwt.expiration}") private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
               .signWith(getKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    public String extractUsername(String token) { return parseClaims(token).getSubject(); }

    public boolean validateToken(String token, UserDetails ud) {
        try {
            return extractUsername(token).equals(ud.getUsername())
                   && !parseClaims(token).getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage()); return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }
    private Key getKey() { return Keys.hmacShaKeyFor(jwtSecret.getBytes()); }
}
