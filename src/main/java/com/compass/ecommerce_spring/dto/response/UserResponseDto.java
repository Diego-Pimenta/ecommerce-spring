package com.compass.ecommerce_spring.dto.response;

import com.compass.ecommerce_spring.entity.enums.Role;

public record UserResponseDto(
        String cpf,
        String name,
        String email,
        String password,
        String phoneNumber,
        String address,
        Role role
) {
}
