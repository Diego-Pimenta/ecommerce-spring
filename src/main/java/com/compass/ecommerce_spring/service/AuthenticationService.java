package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.request.ConfirmPasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.request.PasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import jakarta.mail.MessagingException;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    void passwordReset(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException;

    void updatePassword(String resetToken, ConfirmPasswordResetRequestDto confirmPasswordResetRequestDto);
}
