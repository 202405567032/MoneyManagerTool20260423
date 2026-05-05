package com.personalfinance.accountingsystem.controller;

import com.personalfinance.accountingsystem.dto.*;
import com.personalfinance.accountingsystem.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/monthly-report")
    public ApiResponse<MonthlyReportDto> monthlyReport(@RequestParam String yearMonth) {
        return ApiResponse.success(statisticsService.getMonthlyReport(yearMonth));
    }

    @GetMapping("/expense-pie")
    public ApiResponse<PieChartDto> expensePieChart(@RequestParam String yearMonth) {
        return ApiResponse.success(statisticsService.getExpensePieChart(yearMonth));
    }

    @GetMapping("/trend")
    public ApiResponse<LineChartDto> trend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ApiResponse.success(statisticsService.getTrend(start, end));
    }
}
