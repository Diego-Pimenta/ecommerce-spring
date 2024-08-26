package com.compass.ecommerce_spring.dto.request;

import com.compass.ecommerce_spring.entity.enums.Role;
import com.compass.ecommerce_spring.validation.ValidUserRole;

public record UpdateUserRoleRequestDto(
        @ValidUserRole
        Role role
) {
}
