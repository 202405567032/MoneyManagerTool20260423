package com.personalfinance.accountingsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budget", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "category", "month"})
})
@Data
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String category;

    @Column(name = "month_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthLimit;

    @Column(nullable = false, length = 7)
    private String month;   // format: yyyy-MM

    public Budget(Long userId, String category, BigDecimal monthLimit, String month) {
        this.userId = userId;
        this.category = category;
        this.monthLimit = monthLimit;
        this.month = month;
    }
}
