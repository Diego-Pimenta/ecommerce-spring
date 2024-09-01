package com.compass.ecommerce_spring.validation;

import com.compass.ecommerce_spring.dto.request.SaleItemRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class UniqueProductIdValidator implements ConstraintValidator<UniqueProductId, Set<SaleItemRequestDto>> {

    @Override
    public boolean isValid(Set<SaleItemRequestDto> items, ConstraintValidatorContext context) {
        var productIds = new HashSet<Long>();
        for (SaleItemRequestDto item : items) {
            if (!productIds.add(item.productId())) {
                return false;
            }
        }
        return true;
    }
}
