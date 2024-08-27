package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockStatusRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto save(CreateUserRequestDto createUserRequestDto);

    UserResponseDto findByCpf(String cpf);

    List<UserResponseDto> findAll();

    UserResponseDto update(String cpf, UpdateUserRequestDto updateUserRequestDto);

    UserResponseDto updatePassword(String cpf, UpdateUserPasswordRequestDto updateUserPasswordRequestDto);

    UserResponseDto updateRole(String cpf, UpdateUserRoleRequestDto updateUserRoleRequestDto);

    void delete(String cpf);
}
