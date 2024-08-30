package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.response.SaleItemResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import com.compass.ecommerce_spring.entity.Sale;
import com.compass.ecommerce_spring.entity.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = ProductStockMapper.class)
public interface SaleItemMapper extends MonetaryOperations {

    @Mappings({
            @Mapping(target = "id.sale", source = "sale"),
            @Mapping(target = "id.product", source = "product"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "price", expression = "java(product.getUnitPrice())"),
            @Mapping(target = "discount", expression = "java(getDiscountPercentage(quantity))")
    })
    SaleItem toEntity(Sale sale, ProductStock product, Integer quantity);

    @Mappings({
            @Mapping(target = "product", source = "id.product"),
            @Mapping(target = "subTotal", expression = "java(calculateSubTotal(saleItem))")
    })
    SaleItemResponseDto toDto(SaleItem saleItem);
}
