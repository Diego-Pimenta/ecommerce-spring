package com.compass.ecommerce_spring.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        long expirationTime
) {
}
