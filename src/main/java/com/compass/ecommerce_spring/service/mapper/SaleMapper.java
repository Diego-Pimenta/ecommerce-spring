package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.response.ReportResponseDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;
import com.compass.ecommerce_spring.entity.Sale;
import com.compass.ecommerce_spring.entity.SaleItem;
import com.compass.ecommerce_spring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class, SaleItemMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleMapper extends MonetaryOperations {

    @Mappings({
            @Mapping(target = "moment", expression = "java(LocalDateTime.now())"),
            @Mapping(target = "status", expression = "java(SaleStatus.WAITING_PAYMENT)")
    })
    Sale toEntity(User customer);

    @Mapping(target = "total", expression = "java(calculateTotal(sale))")
    SaleResponseDto toDto(Sale sale);

    @Mapping(target = "total", expression = "java(calculateTotal(sale))")
    ReportResponseDto toReportDto(Sale sale);
}
