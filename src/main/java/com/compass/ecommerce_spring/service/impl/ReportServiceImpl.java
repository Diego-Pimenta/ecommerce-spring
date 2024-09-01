package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.response.ReportResponseDto;
import com.compass.ecommerce_spring.entity.enums.Role;
import com.compass.ecommerce_spring.exception.custom.BusinessException;
import com.compass.ecommerce_spring.repository.SaleRepository;
import com.compass.ecommerce_spring.service.ReportService;
import com.compass.ecommerce_spring.service.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Override
    public List<ReportResponseDto> findByDate(String cpf, LocalDate date) {
        checkPermission(cpf);

        return repository.findAllFetchItems()
                .stream()
                .filter(sale -> sale.getMoment().toLocalDate().isEqual(date))
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDto> findByMonth(String cpf, YearMonth month) {
        checkPermission(cpf);

        return repository.findAllFetchItems()
                .stream()
                .filter(sale -> YearMonth.from(sale.getMoment()).equals(month))
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDto> findByCurrentWeek(String cpf) {
        checkPermission(cpf);

        var now = LocalDate.now();
        var startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        var endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).atTime(LocalTime.MAX);

        return repository.findByWeekRange(startOfWeek, endOfWeek)
                .stream()
                .map(mapper::toReportDto)
                .collect(Collectors.toList());
    }

    private String retrieveUserCpfFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private Role retrieveUserRoleFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow();
    }

    private void checkPermission(String cpf) {
        if (retrieveUserRoleFromToken().equals(Role.CLIENT) && !retrieveUserCpfFromToken().equals(cpf)) {
            throw new BusinessException("Cannot access reports of other users");
        }
    }
}
