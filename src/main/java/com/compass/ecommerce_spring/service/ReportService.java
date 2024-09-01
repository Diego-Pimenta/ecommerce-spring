package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.response.ReportResponseDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {

    List<ReportResponseDto> findByDate(String cpf, LocalDate date);

    List<ReportResponseDto> findByMonth(String cpf, YearMonth month);

    List<ReportResponseDto> findByCurrentWeek(String cpf);
}
