package com.personalfinance.accountingsystem.repository;

import com.personalfinance.accountingsystem.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryAndMonth(Long userId, String category, String month);
    List<Budget> findByUserIdAndMonth(Long userId, String month);
}
