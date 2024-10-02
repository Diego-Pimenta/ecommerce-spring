package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring", imports = StringUtils.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", expression = "java(Role.CLIENT)")
    @Mapping(target = "active", constant = "true")
    User createUserToEntity(CreateUserRequestDto createUserRequestDto);

    @Mappings({
            @Mapping(target = "email", expression = "java(StringUtils.hasText(updateUserRequestDto.email()) ? updateUserRequestDto.email() : user.getEmail())"),
            @Mapping(target = "password", expression = "java(StringUtils.hasText(updateUserRequestDto.password()) ? updateUserRequestDto.password() : user.getPassword())"),
            @Mapping(target = "phoneNumber", expression = "java(StringUtils.hasText(updateUserRequestDto.phoneNumber()) ? updateUserRequestDto.phoneNumber() : user.getPhoneNumber())"),
            @Mapping(target = "address", expression = "java(StringUtils.hasText(updateUserRequestDto.address()) ? updateUserRequestDto.address() : user.getAddress())"),
    })
    User updateUserToEntity(User user, UpdateUserRequestDto updateUserRequestDto);

    UserResponseDto toDto(User user);
}
