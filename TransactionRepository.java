package com.personalfinance.accountingsystem.repository;

import com.personalfinance.accountingsystem.entity.Transaction;
import com.personalfinance.accountingsystem.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end, Pageable pageable);
    Page<Transaction> findByUserIdAndType(Long userId, TransactionType type, Pageable pageable);
    Page<Transaction> findByUserId(Long userId, Pageable pageable);
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT FUNCTION('DATE_FORMAT', t.date, '%Y-%m') as month, " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as totalIncome, " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as totalExpense " +
            "FROM Transaction t WHERE t.userId = :userId AND t.date BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('DATE_FORMAT', t.date, '%Y-%m') ORDER BY month")
    List<Object[]> getMonthlySummary(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT t.category, SUM(t.amount) as total FROM Transaction t " +
            "WHERE t.userId = :userId AND t.type = 'EXPENSE' AND t.date BETWEEN :start AND :end " +
            "GROUP BY t.category")
    List<Object[]> getExpenseByCategory(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT FUNCTION('DATE_FORMAT', t.date, '%Y-%m-%d') as day, " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as expense " +
            "FROM Transaction t WHERE t.userId = :userId AND t.date BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('DATE_FORMAT', t.date, '%Y-%m-%d') ORDER BY day")
    List<Object[]> getDailyTrend(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
