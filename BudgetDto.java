package com.personalfinance.accountingsystem.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class BudgetDto {
    @NotBlank(message = "分类不能为空")
    private String category;

    @NotNull(message = "月度限额不能为空")
    @Positive(message = "限额必须为正数")
    private BigDecimal monthLimit;

    @NotBlank(message = "月份不能为空，格式 yyyy-MM")
    private String month;   // 例如 "2025-05"
}
