package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.request.ConfirmPasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.request.PasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import com.compass.ecommerce_spring.entity.PasswordResetToken;
import com.compass.ecommerce_spring.entity.User;
import com.compass.ecommerce_spring.exception.custom.InvalidTokenException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.PasswordResetTokenRepository;
import com.compass.ecommerce_spring.repository.UserRepository;
import com.compass.ecommerce_spring.service.AuthenticationService;
import com.compass.ecommerce_spring.service.EmailService;
import com.compass.ecommerce_spring.service.mapper.AuthenticationMapper;
import com.compass.ecommerce_spring.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthenticationMapper mapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        var userDetails = userDetailsService.loadUserByUsername(authenticationRequestDto.cpf());

        if (!passwordEncoder.matches(authenticationRequestDto.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequestDto.cpf(),
                authenticationRequestDto.password())
        );

        var token = jwtUtil.createToken(userDetails);
        var expiration = jwtUtil.getExpirationTime();

        return mapper.toAuthResponse(token, expiration);
    }

    @Override
    public void passwordReset(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {
        var email = passwordResetRequestDto.email();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var resetToken = tokenRepository.findByUser(user).isPresent() ? refreshPasswordResetToken(user) : createPasswordResetToken(user);

        var subject = "Password Reset Request";
        var url = "http://localhost:8080/api/v1/auth/reset-password/confirm?token=" + resetToken;
        var body = "<p>Dear " + user.getName() + ",</p>" +
                "<p><strong>You recently requested to reset your password.</strong> " +
                "Please click on the link below to proceed:</p>" +
                "<a href=\"" + url + "\">Reset password</a>" +
                "<p><em>This link will expire in 1h. " +
                "If you did not request this change, please ignore this email.</em></p>" +
                "<p>Best regards,<br>Compass UOL Team</p>";

        emailService.sendPasswordResetEmail(email, subject, body);
    }

    @Transactional
    @Override
    public void updatePassword(String resetToken, ConfirmPasswordResetRequestDto confirmPasswordResetRequestDto) {
        var token = tokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new ResourceNotFoundException("Reset token not found"));

        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token is expired");
        }

        var user = token.getUser();
        user.setPassword(passwordEncoder.encode(confirmPasswordResetRequestDto.newPassword()));
        userRepository.save(user);
        tokenRepository.delete(token);
    }

    @Transactional
    private String createPasswordResetToken(User user) {
        var token = UUID.randomUUID().toString();
        var resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        return token;
    }

    @Transactional
    private String refreshPasswordResetToken(User user) {
        var token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Reset token not found"));

        token.setToken(UUID.randomUUID().toString());
        token.setExpirationTime(LocalDateTime.now().plusMinutes(60));
        tokenRepository.save(token);

        return token.getToken();
    }
}
