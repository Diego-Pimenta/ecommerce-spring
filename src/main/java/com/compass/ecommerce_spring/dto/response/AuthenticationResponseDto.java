package com.compass.ecommerce_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"token", "expiration"})
public record AuthenticationResponseDto(
        String token,
        long expiration
) {
}
