package com.compass.ecommerce_spring.common;

import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductConstants {

    public static final ProductStock PRODUCT = new ProductStock(2L, "Cigarettes", 13, new BigDecimal("3.99"), "Drugs", true, new HashSet<>());
    public static final List<ProductStock> PRODUCTS = new ArrayList<>() {
        {
            add(PRODUCT);
        }
    };

    public static final ProductStockResponseDto PRODUCT_RESPONSE_DTO = new ProductStockResponseDto(2L, "Cigarettes", 13, new BigDecimal("3.99"), "Drugs", true);
    public static final List<ProductStockResponseDto> PRODUCTS_RESPONSE_DTO = new ArrayList<>() {
        {
            add(PRODUCT_RESPONSE_DTO);
        }
    };

    public static final ProductStock BATTERIES = new ProductStock(1L, "Batteries", 8, new BigDecimal("1.99"), "Electronics", true, new HashSet<>());
}
