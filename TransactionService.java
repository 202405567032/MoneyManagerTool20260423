package com.personalfinance.accountingsystem.service;

import com.personalfinance.accountingsystem.dto.TransactionDto;
import com.personalfinance.accountingsystem.dto.TransactionResponse;
import com.personalfinance.accountingsystem.entity.Transaction;
import com.personalfinance.accountingsystem.entity.TransactionType;
import com.personalfinance.accountingsystem.repository.TransactionRepository;
import com.personalfinance.accountingsystem.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetService budgetService;

    public TransactionResponse addTransaction(TransactionDto dto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Transaction transaction = new Transaction(userId, dto.getType(), dto.getAmount(),
                dto.getCategory(), dto.getDate(), dto.getRemark());
        Transaction saved = transactionRepository.save(transaction);

        // 超额提醒（仅支出）
        TransactionResponse response = TransactionResponse.fromEntity(saved);
        if (dto.getType() == TransactionType.EXPENSE) {
            String month = dto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            var warning = budgetService.checkOverBudget(userId, dto.getCategory(), dto.getAmount(), month);
            if (warning != null && warning.isOverLimit()) {
                response.setWarning("超出预算！分类:" + warning.getCategory() + " 限额:" + warning.getLimit() +
                        " 已花费:" + warning.getCurrentSpent() + " 本次后总额:" + warning.getAfterAmount());
            }
        }
        return response;
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionDto dto) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!existing.getUserId().equals(UserContextHolder.getCurrentUserId())) {
            throw new RuntimeException("No permission");
        }
        existing.setType(dto.getType());
        existing.setAmount(dto.getAmount());
        existing.setCategory(dto.getCategory());
        existing.setDate(dto.getDate());
        existing.setRemark(dto.getRemark());
        Transaction saved = transactionRepository.save(existing);
        TransactionResponse response = TransactionResponse.fromEntity(saved);
        if (dto.getType() == TransactionType.EXPENSE) {
            String month = dto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            var warning = budgetService.checkOverBudget(existing.getUserId(), dto.getCategory(), dto.getAmount(), month);
            if (warning != null && warning.isOverLimit()) {
                response.setWarning("更新后超出预算！");
            }
        }
        return response;
    }

    public void deleteTransaction(Long id) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!existing.getUserId().equals(UserContextHolder.getCurrentUserId())) {
            throw new RuntimeException("No permission");
        }
        transactionRepository.deleteById(id);
    }

    public Page<Transaction> getUserTransactions(Long userId, LocalDate start, LocalDate end, Pageable pageable) {
        return transactionRepository.findByUserIdAndDateBetween(userId, start, end, pageable);
    }
}
