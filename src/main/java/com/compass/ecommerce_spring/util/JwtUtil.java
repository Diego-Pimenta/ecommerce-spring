package com.compass.ecommerce_spring.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

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
        var authority = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        var username = userDetails.getUsername();
        var issuedAt = new Date(System.currentTimeMillis());
        var expiration = new Date(System.currentTimeMillis() + getExpirationTime());

        return Jwts
                .builder()
                .claim("authority", authority)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
