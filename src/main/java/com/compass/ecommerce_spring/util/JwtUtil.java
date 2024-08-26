package com.compass.ecommerce_spring.util;

import com.compass.ecommerce_spring.security.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String createToken(UserDetails userDetails) {
        var authorities = userDetails.getAuthorities();
        var username = userDetails.getUsername();
        var issuedAt = new Date(System.currentTimeMillis());
        var expiration = new Date(System.currentTimeMillis() + getExpirationTime());

        return Jwts
                .builder()
                .claim("authorities", authorities)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    public long getExpirationTime() {
        return jwtProperties.expirationTime();
    }

    public boolean isTokenValid(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
