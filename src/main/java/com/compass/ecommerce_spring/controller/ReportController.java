package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.dto.response.ReportResponseDto;
import com.compass.ecommerce_spring.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "Report", description = "Contains operations related with the report")
public interface ReportController {
    @Operation(summary = "Retrieve all sales by date",
            description = "Get all sale objects by specifying its date",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All sales successfully retrieved by date",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<ReportResponseDto>> findByDate(String cpf, LocalDate date);

    @Operation(summary = "Retrieve all sales by month",
            description = "Get all sale objects by specifying its month",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All sales successfully retrieved by month",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<ReportResponseDto>> findByMonth(String cpf, YearMonth month);

    @Operation(summary = "Retrieve all sales by current week",
            description = "Get all sale objects by current week",
            security = @SecurityRequirement(name = "security"),
            tags = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All sales successfully retrieved by current week",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    ResponseEntity<List<ReportResponseDto>> findByCurrentWeek(String cpf);
}
