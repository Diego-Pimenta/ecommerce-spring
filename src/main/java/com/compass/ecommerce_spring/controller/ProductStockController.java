package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.request.ProductStockRequestDto;
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

@Tag(name = "Product Stock", description = "Contains everything related to operations with product stock")
public interface ProductStockController {
    @Operation(summary = "Create a new product in stock",
            description = "Save a product object with body content",
//            security = @SecurityRequirement(name = "security"),
            tags = {"Products", "Post"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product successfully created in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied by insufficient permissions",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Product not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> save(ProductStockRequestDto productStockRequestDto);
    @Operation(summary = "Retrieve a product in stock by id",
            description = "Get a product object by specifying its id",
//            security = @SecurityRequirement(name = "security"),
            tags = {"Products", "Get"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully retrieved from stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied by insufficient permissions",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> findById(Long id);
    @Operation(summary = "Retrieve all products in stocks",
            description = "Get all product objects",
//            security = @SecurityRequirement(name = "security"),
            tags = {"Products", "Get"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "All products successfully retrieved from stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied by insufficient permissions",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<ProductStockResponseDto>> findAll();
    @Operation(summary = "Update a product in stock by id",
            description = "Modify a product object with body content by its id",
//            security = @SecurityRequirement(name = "security"),
            tags = {"Products", "Put"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully updated in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied by insufficient permissions",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Product not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<ProductStockResponseDto> update(Long id, ProductStockRequestDto updateProductStockRequestDto);
    @Operation(summary = "Delete a product in stock by id",
            description = "Remove a product object by specifying its id",
//            security = @SecurityRequirement(name = "security"),
            tags = {"Products", "Delete"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product successfully deleted from stock",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Access denied by insufficient permissions",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in stock",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<Void> delete(Long id);
}
