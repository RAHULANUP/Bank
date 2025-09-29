package com.bank.Bank.mapper;

import org.springframework.stereotype.Component;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Transaction;

@Component
public class TransactionMapper {
    public Transaction toEntity(TransactionDto transactionDto) {
        if (transactionDto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());
        transaction.setTransactionDate(transactionDto.getTransactionDate());

        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setType(transaction.getType());
        transactionDto.setTransactionDate(transaction.getTransactionDate());

        if (transaction.getCustomer() != null) {
            transactionDto.setCustomerId(transaction.getCustomer().getCustomerId());
            transactionDto.setCustomerName(transaction.getCustomer().getCustomerName());
        }
        if (transaction.getAccount() != null) {
            transactionDto.setAccountNumber(transaction.getAccount().getAccountNumber());
            transactionDto.setAccountBalance(transaction.getAccount().getBalance());
        }

        return transactionDto;
    }
}
