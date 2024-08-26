package com.compass.ecommerce_spring.validation;

import com.compass.ecommerce_spring.entity.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<ValidUserRole, Role> {

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        if (role == null) {
            return false;
        }

        return Role.CLIENT.equals(role) || Role.ADMIN.equals(role);
    }
}
