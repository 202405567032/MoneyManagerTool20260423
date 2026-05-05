package com.personalfinance.accountingsystem.controller;

import com.personalfinance.accountingsystem.dto.ApiResponse;
import com.personalfinance.accountingsystem.dto.TransactionDto;
import com.personalfinance.accountingsystem.dto.TransactionResponse;
import com.personalfinance.accountingsystem.entity.Transaction;
import com.personalfinance.accountingsystem.service.TransactionService;
import com.personalfinance.accountingsystem.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionResponse> addTransaction(@Valid @RequestBody TransactionDto dto) {
        return ApiResponse.success(transactionService.addTransaction(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<TransactionResponse> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDto dto) {
        return ApiResponse.success(transactionService.updateTransaction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ApiResponse.success("Deleted successfully", null);
    }

    @GetMapping
    public ApiResponse<Page<Transaction>> listTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Pageable pageable) {
        Long userId = UserContextHolder.getCurrentUserId();
        if (start == null) start = LocalDate.of(2000, 1, 1);
        if (end == null) end = LocalDate.now();
        Page<Transaction> page = transactionService.getUserTransactions(userId, start, end, pageable);
        return ApiResponse.success(page);
    }
}
