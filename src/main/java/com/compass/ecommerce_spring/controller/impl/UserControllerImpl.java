package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.UserController;
import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateActiveStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserControllerImpl implements UserController {

    private final UserService service;

    @PostMapping
    @Override
    public ResponseEntity<UserResponseDto> save(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        UserResponseDto userResponseDto = service.save(createUserRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}")
                .buildAndExpand(userResponseDto.cpf()).toUri();
        return ResponseEntity.created(uri).body(userResponseDto);
    }

    @GetMapping("/{cpf}")
    @Override
    public ResponseEntity<UserResponseDto> findByCpf(@PathVariable("cpf") String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{cpf}")
    @Override
    public ResponseEntity<UserResponseDto> update(
            @PathVariable("cpf") String cpf,
            @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto
    ) {
        return ResponseEntity.ok(service.update(cpf, updateUserRequestDto));
    }

    @PatchMapping("/change-password/{cpf}")
    @Override
    public ResponseEntity<UserResponseDto> updatePassword(
            @PathVariable("cpf") String cpf,
            @RequestBody @Valid UpdateUserPasswordRequestDto updateUserPasswordRequestDto
    ) {
        return ResponseEntity.ok(service.updatePassword(cpf, updateUserPasswordRequestDto));
    }

    @PatchMapping("/role/{cpf}")
    @Override
    public ResponseEntity<UserResponseDto> updateRole(
            @PathVariable("cpf") String cpf,
            @RequestBody @Valid UpdateUserRoleRequestDto updateUserRoleRequestDto
    ) {
        return ResponseEntity.ok(service.updateRole(cpf, updateUserRoleRequestDto));
    }

    @PatchMapping("/status/{cpf}")
    @Override
    public ResponseEntity<UserResponseDto> updateStatus(
            @PathVariable("cpf") String cpf,
            @RequestBody @Valid UpdateActiveStatusRequestDto updateActiveStatusRequestDto
    ) {
        return ResponseEntity.ok(service.updateStatus(cpf, updateActiveStatusRequestDto));
    }

    @DeleteMapping("/{cpf}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("cpf") String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
