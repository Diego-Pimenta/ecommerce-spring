package com.compass.ecommerce_spring.dto.response;

public record AuthenticationResponseDto(
        String token,
        long expiration
) {
}
