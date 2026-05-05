package com.personalfinance.accountingsystem.dto;

import com.personalfinance.accountingsystem.entity.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long userId;
    private String type;      // "INCOME" 或 "EXPENSE"
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String remark;
    private String warning;   // 超额提醒信息（可选）

    public static TransactionResponse fromEntity(Transaction transaction) {
        TransactionResponse resp = new TransactionResponse();
        resp.setId(transaction.getId());
        resp.setUserId(transaction.getUserId());
        resp.setType(transaction.getType().name());
        resp.setAmount(transaction.getAmount());
        resp.setCategory(transaction.getCategory());
        resp.setDate(transaction.getDate());
        resp.setRemark(transaction.getRemark());
        return resp;
    }
}
