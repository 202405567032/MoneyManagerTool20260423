package com.personalfinance.accountingsystem.service;

import com.personalfinance.accountingsystem.dto.*;
import com.personalfinance.accountingsystem.entity.TransactionType;
import com.personalfinance.accountingsystem.repository.TransactionRepository;
import com.personalfinance.accountingsystem.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public MonthlyReportDto getMonthlyReport(String yearMonth) {
        Long userId = UserContextHolder.getCurrentUserId();
        LocalDate start = LocalDate.parse(yearMonth + "-01");
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Object[]> categoryData = transactionRepository.getExpenseByCategory(userId, start, end);
        BigDecimal totalIncome = transactionRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = transactionRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<CategoryExpenseDto> categoryExpenses = new ArrayList<>();
        for (Object[] row : categoryData) {
            String cat = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            double percent = totalExpense.compareTo(BigDecimal.ZERO) == 0 ? 0 :
                    amount.divide(totalExpense, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
            categoryExpenses.add(new CategoryExpenseDto(cat, amount, percent));
        }
        return new MonthlyReportDto(yearMonth, totalIncome, totalExpense, balance, categoryExpenses);
    }

    public PieChartDto getExpensePieChart(String yearMonth) {
        MonthlyReportDto report = getMonthlyReport(yearMonth);
        List<PieDataDto> data = report.getCategoryExpenses().stream()
                .map(c -> new PieDataDto(c.getCategory(), c.getAmount()))
                .collect(Collectors.toList());
        return new PieChartDto(data);
    }

    public LineChartDto getTrend(LocalDate start, LocalDate end) {
        Long userId = UserContextHolder.getCurrentUserId();
        List<Object[]> dailyData = transactionRepository.getDailyTrend(userId, start, end);
        List<String> dates = new ArrayList<>();
        List<BigDecimal> incomes = new ArrayList<>();
        List<BigDecimal> expenses = new ArrayList<>();
        for (Object[] row : dailyData) {
            dates.add((String) row[0]);
            incomes.add((BigDecimal) row[1]);
            expenses.add((BigDecimal) row[2]);
        }
        return new LineChartDto(dates, incomes, expenses);
    }
}