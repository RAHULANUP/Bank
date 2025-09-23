package com.bank.Bank.controller;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TransactionControllerUnitTest {
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionsByAccount_shouldReturnOk() {
        when(transactionService.getTransactionsByAccountId(anyLong()))
                .thenReturn(Collections.singletonList(new TransactionDto()));
        ResponseEntity<List<TransactionDto>> response = transactionController.getTransactionsByAccount(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}
