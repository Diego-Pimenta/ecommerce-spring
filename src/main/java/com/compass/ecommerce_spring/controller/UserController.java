package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.request.CreateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserPasswordRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateUserRoleRequestDto;
import com.compass.ecommerce_spring.dto.response.UserResponseDto;
import com.compass.ecommerce_spring.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "User", description = "Contains operations related with the user")
public interface UserController {
    @Operation(summary = "Create a new user",
            description = "Save an user object with body content",
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            })
    ResponseEntity<UserResponseDto> save(CreateUserRequestDto createUserRequestDto);

    @Operation(summary = "Retrieve an user by cpf",
            description = "Get an user object by specifying its cpf",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<UserResponseDto> findByCpf(String cpf);

    @Operation(summary = "Retrieve all users",
            description = "Get all user objects",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All users successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<UserResponseDto>> findAll();

    @Operation(summary = "Update an user by cpf",
            description = "Modify an user object with body content by its cpf",
            security = @SecurityRequirement(name = "security"),
            tags = "Put",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<UserResponseDto> update(String cpf, UpdateUserRequestDto updateUserRequestDto);

    @Operation(summary = "Update an user password by cpf",
            description = "Modify an user object with body content by its cpf",
            tags = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "User credentials don't match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<UserResponseDto> updatePassword(String cpf, UpdateUserPasswordRequestDto updateUserPasswordRequestDto);

    @Operation(summary = "Update an user role by cpf",
            description = "Modify an user object with body content by its cpf",
            security = @SecurityRequirement(name = "security"),
            tags = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<UserResponseDto> updateRole(String cpf, UpdateUserRoleRequestDto updateUserRoleRequestDto);

    @Operation(summary = "Delete an user by cpf",
            description = "Remove an user object by specifying its cpf",
            security = @SecurityRequirement(name = "security"),
            tags = "Delete",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User successfully deleted",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> delete(String cpf);
}
