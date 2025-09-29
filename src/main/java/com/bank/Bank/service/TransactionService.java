package com.bank.Bank.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Transaction;
import com.bank.Bank.mapper.TransactionMapper;
import com.bank.Bank.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountNumber(Long accountNumber) {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(tx -> tx.getAccount() != null && tx.getAccount().getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
        return transactions.stream().map(transactionMapper::toDto).collect(Collectors.toList());
    }
}
