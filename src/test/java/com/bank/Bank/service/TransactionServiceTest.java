package com.bank.Bank.service;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Account;
import com.bank.Bank.entity.Transaction;
import com.bank.Bank.mapper.TransactionMapper;
import com.bank.Bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionsByAccountId_shouldReturnList() {
        Transaction tx = new Transaction();
        Account acc = new Account();
        acc.setId(1L);
        tx.setAccount(acc);
        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(tx));
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(new TransactionDto());
        List<TransactionDto> result = transactionService.getTransactionsByAccountId(1L);
        assertEquals(1, result.size());
    }
}
