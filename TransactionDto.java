package com.personalfinance.accountingsystem.dto;

import com.personalfinance.accountingsystem.entity.TransactionType;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionDto {
    @NotNull(message = "类型不能为空")
    private TransactionType type;

    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须为正数")
    private BigDecimal amount;

    @NotBlank(message = "分类不能为空")
    private String category;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    private String remark;
}
