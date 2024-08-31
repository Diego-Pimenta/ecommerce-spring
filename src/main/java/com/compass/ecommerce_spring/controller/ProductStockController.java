package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Product Stock", description = "Contains operations related with the product stock")
public interface ProductStockController {
    @Operation(summary = "Create a new product in stock",
            description = "Save a product object with body content",
            security = @SecurityRequirement(name = "security"),
            tags = "Post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product successfully created in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid operation with data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict with existing data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> save(CreateProductStockRequestDto createProductStockRequestDto);

    @Operation(summary = "Retrieve a product in stock by id",
            description = "Get a product object by specifying its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully retrieved from stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> findById(Long id);

    @Operation(summary = "Retrieve all products in stocks",
            description = "Get all product objects",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All products successfully retrieved from stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<ProductStockResponseDto>> findAll();

    @Operation(summary = "Update a product in stock by id",
            description = "Modify a product object with body content by its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Put",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully updated in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid operation with data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict with existing data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> update(Long id, UpdateProductStockRequestDto updateProductStockRequestDto);

    @Operation(summary = "Update a product status in stock by id",
            description = "Modify a product object with body content by its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully updated in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid operation with data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> updateStatus(Long id, UpdateProductStockStatusRequestDto updateProductStockStatusRequestDto);

    @Operation(summary = "Delete a product in stock by id",
            description = "Remove a product object by specifying its id",
            security = @SecurityRequirement(name = "security"),
            tags = "Delete",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product successfully deleted from stock",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> delete(Long id);
}
