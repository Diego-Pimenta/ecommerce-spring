package com.compass.ecommerce_spring.dto.request;

import com.compass.ecommerce_spring.entity.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequestDto(
        @NotNull(message = "Invalid role: Role must be CLIENT or ADMIN")
        Role role
) {
}
