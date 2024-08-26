package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import com.compass.ecommerce_spring.service.AuthenticationService;
import com.compass.ecommerce_spring.service.mapper.AuthenticationMapper;
import com.compass.ecommerce_spring.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthenticationMapper mapper;

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
}
