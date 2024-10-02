package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.dto.request.ConfirmPasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.request.PasswordResetRequestDto;
import com.compass.ecommerce_spring.dto.response.AuthenticationResponseDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Operation of authentication for the user")
public interface AuthenticationController {

    @Operation(summary = "Authenticate an user",
            description = "Generates a jwt token with permissions to the user",
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "User credentials don't match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<AuthenticationResponseDto> authentication(AuthenticationRequestDto authenticationRequestDto);

    @Operation(summary = "Request reset token",
            description = "Generates and sends a reset token to the user's email",
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password reset successfully requested",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> resetPassword(PasswordResetRequestDto passwordResetRequestDto) throws MessagingException;

    @Operation(summary = "Confirm password reset",
            description = "Modify an user object with body content using reset token",
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User password successfully updated",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Reset token is expired",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Reset token not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> updatePassword(String resetToken, ConfirmPasswordResetRequestDto confirmPasswordResetRequestDto);
}
