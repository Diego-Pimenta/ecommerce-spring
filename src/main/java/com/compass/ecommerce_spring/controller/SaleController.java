package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.request.CreateSaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;
import com.compass.ecommerce_spring.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Sale", description = "Contains operations related with the sale")
public interface SaleController {
    @Operation(summary = "Create a new sale",
            description = "Save a sale object with body content",
            security = @SecurityRequirement(name = "security"),
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sale successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid sale data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Sale data not achievable",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<SaleResponseDto> save(CreateSaleRequestDto createSaleRequestDto);

    @Operation(summary = "Retrieve a sale by id",
            description = "Get a sale object by specifying its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<SaleResponseDto> findById(Long id);

    @Operation(summary = "Retrieve all sales",
            description = "Get all sale objects",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All sales successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<SaleResponseDto>> findAll();

    @Operation(summary = "Update a sale by id",
            description = "Modify a sale object with body content by its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Put",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid sale data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Sale data not achievable",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<SaleResponseDto> update(Long id, UpdateSaleRequestDto updateSaleRequestDto);

    @Operation(summary = "Update a sale status by id",
            description = "Modify a sale object with body content by its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid sale data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Sale data not achievable",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<SaleResponseDto> updateStatus(Long id, UpdateSaleStatusRequestDto updateSaleStatusRequestDto);

    @Operation(summary = "Delete a sale by id",
            description = "Remove a sale object by specifying its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Delete",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sale successfully deleted",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> delete(Long id);
}
