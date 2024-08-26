package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponseDto save(CreateUserRequestDto createUserRequestDto) {
        return null;
    }

    @Override
    public UserResponseDto findById(Long id) {
        return null;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return List.of();
    }

    @Override
    public UserResponseDto update(Long id, UpdateUserRequestDto updateUserRequestDto) {
        return null;
    }

    @Override
    public UserResponseDto updatePassword(Long id, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        return null;
    }

    @Override
    public UserResponseDto updateRole(Long id, UpdateUserRoleRequestDto updateUserRoleRequestDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
