package com.personalfinance.accountingsystem.service;

import com.personalfinance.accountingsystem.dto.BudgetDto;
import com.personalfinance.accountingsystem.dto.BudgetWarningDto;
import com.personalfinance.accountingsystem.entity.Budget;
import com.personalfinance.accountingsystem.repository.BudgetRepository;
import com.personalfinance.accountingsystem.repository.TransactionRepository;
import com.personalfinance.accountingsystem.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Budget setBudget(BudgetDto dto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Budget budget = new Budget(userId, dto.getCategory(), dto.getMonthLimit(), dto.getMonth());
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Long id, BudgetDto dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        if (!budget.getUserId().equals(UserContextHolder.getCurrentUserId())) {
            throw new RuntimeException("No permission");
        }
        budget.setMonthLimit(dto.getMonthLimit());
        return budgetRepository.save(budget);
    }

    public List<Budget> getBudgetsByMonth(String month) {
        Long userId = UserContextHolder.getCurrentUserId();
        return budgetRepository.findByUserIdAndMonth(userId, month);
    }

    public BudgetWarningDto checkOverBudget(Long userId, String category, BigDecimal newExpenseAmount, String month) {
        Budget budget = budgetRepository.findByUserIdAndCategoryAndMonth(userId, category, month).orElse(null);
        if (budget == null) return null;

        LocalDate monthStart = LocalDate.parse(month + "-01");
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        BigDecimal totalSpent = transactionRepository.findByUserIdAndDateBetween(userId, monthStart, monthEnd).stream()
                .filter(t -> t.getCategory().equals(category))
                .map(transaction -> transaction.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal afterAdding = totalSpent.add(newExpenseAmount);
        if (afterAdding.compareTo(budget.getMonthLimit()) > 0) {
            return new BudgetWarningDto(true, category, budget.getMonthLimit(), totalSpent, afterAdding);
        }
        return new BudgetWarningDto(false, category, budget.getMonthLimit(), totalSpent, afterAdding);
    }
}