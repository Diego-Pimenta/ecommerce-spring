package com.compass.ecommerce_spring.dto.response;

import com.compass.ecommerce_spring.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(
        String cpf,
        String name,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String address,
        Role role,
        Boolean active
) {
}
