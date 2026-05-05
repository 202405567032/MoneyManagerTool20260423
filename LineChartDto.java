package com.personalfinance.accountingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineChartDto {
    private List<String> dates;      // X轴日期
    private List<BigDecimal> incomes;
    private List<BigDecimal> expenses;
}
