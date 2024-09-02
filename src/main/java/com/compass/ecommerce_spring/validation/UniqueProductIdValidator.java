package com.compass.ecommerce_spring.validation;

import com.compass.ecommerce_spring.dto.request.SaleItemRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UniqueProductIdValidator implements ConstraintValidator<UniqueProductId, List<SaleItemRequestDto>> {

    @Override
    public boolean isValid(List<SaleItemRequestDto> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            return true;
        }

        // mapeia a frequência de cada um dos ids
        var idCountMap = items.stream()
                .map(SaleItemRequestDto::productId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // filtra pelos ids que aparecem mais de uma vez
        var duplicatedIds = idCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey().toString())
                .toList();

        if (!duplicatedIds.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Duplicate product ids found: " + String.join(", ", duplicatedIds))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
