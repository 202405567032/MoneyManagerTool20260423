package com.personalfinance.accountingsystem.controller;

import com.personalfinance.accountingsystem.dto.ApiResponse;
import com.personalfinance.accountingsystem.dto.BudgetDto;
import com.personalfinance.accountingsystem.entity.Budget;
import com.personalfinance.accountingsystem.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ApiResponse<Budget> setBudget(@Valid @RequestBody BudgetDto dto) {
        return ApiResponse.success(budgetService.setBudget(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<Budget> updateBudget(@PathVariable Long id, @Valid @RequestBody BudgetDto dto) {
        return ApiResponse.success(budgetService.updateBudget(id, dto));
    }

    @GetMapping
    public ApiResponse<List<Budget>> getBudgets(@RequestParam String month) {
        return ApiResponse.success(budgetService.getBudgetsByMonth(month));
    }
}
