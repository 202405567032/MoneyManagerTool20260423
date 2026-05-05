package com.personalfinance.accountingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryExpenseDto {
    private String category;
    private BigDecimal amount;
    private double percent;   // 占比百分比，例如 35.5
}
