package com.bank.Bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.Bank.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime transactionDate;
    private Long customerId;
    private String customerName;
    private Long accountId;
    private BigDecimal accountBalance;
}
