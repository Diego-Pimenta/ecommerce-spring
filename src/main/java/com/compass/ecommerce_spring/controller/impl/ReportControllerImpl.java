package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.ReportController;
import com.compass.ecommerce_spring.dto.response.ReportResponseDto;
import com.compass.ecommerce_spring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/reports")
public class ReportControllerImpl implements ReportController {

    private final ReportService service;

    @GetMapping("/user/{cpf}/date/{date}")
    @Override
    public ResponseEntity<List<ReportResponseDto>> findByDate(
            @PathVariable("cpf") String cpf,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(service.findByDate(cpf, date));
    }

    @GetMapping("/user/{cpf}/month/{month}")
    @Override
    public ResponseEntity<List<ReportResponseDto>> findByMonth(
            @PathVariable("cpf") String cpf,
            @PathVariable("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return ResponseEntity.ok(service.findByMonth(cpf, month));
    }

    @GetMapping("/user/{cpf}/current-week")
    @Override
    public ResponseEntity<List<ReportResponseDto>> findByCurrentWeek(@PathVariable("cpf") String cpf) {
        return ResponseEntity.ok(service.findByCurrentWeek(cpf));
    }
}
