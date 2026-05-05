package com.personalfinance.accountingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieDataDto {
    private String name;    // 分类名称
    private BigDecimal value; // 金额
}
