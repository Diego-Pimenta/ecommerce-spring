package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createUserToEntity(CreateUserRequestDto createUserRequestDto);

    User updateUserToEntity(String cpf, UpdateUserRequestDto updateUserRequestDto);

    UserResponseDto toDto(User user);
}
