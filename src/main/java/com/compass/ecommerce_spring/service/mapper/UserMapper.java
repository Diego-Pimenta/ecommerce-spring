package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User createUserToEntity(CreateUserRequestDto createUserRequestDto);

    @Mappings({
            @Mapping(target = "email", expression = "java(updateUserRequestDto.email())"),
            @Mapping(target = "password", expression = "java(updateUserRequestDto.password())"),
            @Mapping(target = "phoneNumber", expression = "java(updateUserRequestDto.phoneNumber())"),
            @Mapping(target = "address", expression = "java(updateUserRequestDto.address())"),
    })
    User updateUserToEntity(User user, UpdateUserRequestDto updateUserRequestDto);

    UserResponseDto toDto(User user);
}
