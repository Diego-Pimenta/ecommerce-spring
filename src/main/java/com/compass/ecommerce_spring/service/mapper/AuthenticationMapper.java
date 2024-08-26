package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    AuthenticationResponseDto toAuthResponse(String token, long expiration);
}
