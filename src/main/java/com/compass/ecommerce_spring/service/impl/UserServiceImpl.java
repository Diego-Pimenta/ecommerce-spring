package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.UserRepository;
import com.compass.ecommerce_spring.service.UserService;
import com.compass.ecommerce_spring.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public UserResponseDto save(CreateUserRequestDto createUserRequestDto) {
        // TODO: fix DataIntegrityViolation (existing email not handled)
        repository.findByCpf(createUserRequestDto.cpf())
                .ifPresent(p -> {
                    throw new ResourceAlreadyExistsException("An user with this cpf already exists");
                });

        var user = mapper.createUserToEntity(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.password()));
        var createdUser = repository.save(user);
        return mapper.toDto(createdUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto update(String cpf, UpdateUserRequestDto updateUserRequestDto) {
        // TODO: fix DataIntegrityViolation (Columns receiving null values)
        repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var user = mapper.updateUserToEntity(cpf, updateUserRequestDto);
        user.setPassword(passwordEncoder.encode(updateUserRequestDto.password()));
        var updatedUser = repository.save(user);
        return mapper.toDto(updatedUser);
    }

    @Override
    public UserResponseDto updatePassword(String cpf, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        var user = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(updateUserPasswordRequestDto.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password don't match");
        }

        user.setPassword(passwordEncoder.encode(updateUserPasswordRequestDto.newPassword()));
        var updateUser = repository.save(user);
        return mapper.toDto(updateUser);
    }

    @Override
    public UserResponseDto updateRole(String cpf, UpdateUserRoleRequestDto updateUserRoleRequestDto) {
        var user = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(updateUserRoleRequestDto.role());
        var updateUser = repository.save(user);
        return mapper.toDto(updateUser);
    }

    @Override
    public void delete(String cpf) {
        repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        repository.deleteById(cpf);
    }
}
