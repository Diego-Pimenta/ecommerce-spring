package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.response.ReportResponseDto;
import com.compass.ecommerce_spring.repository.SaleRepository;
import com.compass.ecommerce_spring.service.ReportService;
import com.compass.ecommerce_spring.service.mapper.SaleMapper;
import com.compass.ecommerce_spring.security.AccessAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {

    private final SaleRepository repository;
    private final SaleMapper mapper;
    private final AccessAuthority accessAuthority;

    @Override
    public List<ReportResponseDto> findByDate(String cpf, LocalDate date) {
        accessAuthority.checkPermission(cpf);

        return repository.findAll().stream()
                .filter(sale -> sale.getMoment().toLocalDate().isEqual(date))
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDto> findByMonth(String cpf, YearMonth month) {
        accessAuthority.checkPermission(cpf);

        return repository.findAll().stream()
                .filter(sale -> YearMonth.from(sale.getMoment()).equals(month))
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDto> findByCurrentWeek(String cpf) {
        accessAuthority.checkPermission(cpf);

        var now = LocalDate.now();
        var startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        var endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).atTime(LocalTime.MAX);

        return repository.findByWeekRange(startOfWeek, endOfWeek).stream()
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }
}
