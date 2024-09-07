package com.zolotarev.phonebook.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("JWT exception " + e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        String subToken = token;
        if (token.startsWith("Bearer ")) {
            subToken = token.substring(7);
        }
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(subToken);
        } catch (ExpiredJwtException e) {
            log.error("JWT expired exception " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT is not valid " + e.getMessage());
        } catch (JwtException e) {
            log.error("JWT is not supported " + e.getMessage());
        }
        return false;
    }

}
