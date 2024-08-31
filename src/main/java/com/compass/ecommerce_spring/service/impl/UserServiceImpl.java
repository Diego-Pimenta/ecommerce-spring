package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.entity.enums.Role;
import com.compass.ecommerce_spring.exception.custom.BusinessException;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.UserRepository;
import com.compass.ecommerce_spring.service.UserService;
import com.compass.ecommerce_spring.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (repository.existsByCpf(createUserRequestDto.cpf())) {
            throw new ResourceAlreadyExistsException("An user with this cpf already exists");
        }

        if (repository.existsByEmail(createUserRequestDto.email())) {
            throw new ResourceAlreadyExistsException("An user with this email already exists");
        }

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
        var existingUser = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        checkPermission(cpf);

        var existingUserByEmail = repository.findByEmail(updateUserRequestDto.email());

        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getCpf().equals(cpf)) {
            throw new ResourceAlreadyExistsException("An user with this email already exists");
        }

        var user = mapper.updateUserToEntity(existingUser, updateUserRequestDto);
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
    public UserResponseDto updateStatus(String cpf, UpdateUserStatusRequestDto updateUserStatusRequestDto) {
        var user = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        checkPermission(cpf); // caso um usuário se inative, apenas um admin pode ativá-lo de volta

        user.setActive(updateUserStatusRequestDto.active());
        var updateUser = repository.save(user);
        return mapper.toDto(updateUser);
    }

    @Override
    public void delete(String cpf) {
        var user = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (retrieveUserCpfFromToken().equals(cpf)) {
            throw new AccessDeniedException("You cannot delete your own account");
        }

        user.setActive(false);
        repository.save(user);
    }

    private String retrieveUserCpfFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private Role retrieveUserRoleFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow();
    }

    private void checkPermission(String cpf) {
        // somente admin pode alterar informações da conta dos outros
        if (retrieveUserRoleFromToken().equals(Role.CLIENT) && !retrieveUserCpfFromToken().equals(cpf)) {
            throw new BusinessException("Account belongs to another user");
        }
    }
}
