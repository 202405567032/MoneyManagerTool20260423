package com.personalfinance.accountingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetWarningDto {
    private boolean overLimit;
    private String category;
    private BigDecimal limit;
    private BigDecimal currentSpent;
    private BigDecimal afterAmount;
}
