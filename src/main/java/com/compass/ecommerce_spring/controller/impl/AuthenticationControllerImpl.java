package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.AuthenticationController;
import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.request.ConfirmPasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.request.PasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import com.compass.ecommerce_spring.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/reset-password")
    @Override
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid PasswordResetRequestDto passwordResetRequestDto
    ) throws MessagingException {
        service.passwordReset(passwordResetRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/confirm")
    @Override
    public ResponseEntity<Void> updatePassword(
            @RequestParam("token") String resetToken,
            @RequestBody @Valid ConfirmPasswordResetRequestDto confirmPasswordResetRequestDto
    ) {
        service.updatePassword(resetToken, confirmPasswordResetRequestDto);
        return ResponseEntity.noContent().build();
    }
}
