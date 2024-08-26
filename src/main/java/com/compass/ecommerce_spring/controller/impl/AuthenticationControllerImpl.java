package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.AuthenticationController;
import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import com.compass.ecommerce_spring.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    @Override
    public ResponseEntity<AuthenticationResponseDto> authentication(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(service.authenticate(authenticationRequestDto));
    }
}
