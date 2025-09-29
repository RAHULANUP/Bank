package com.bank.Bank.controller;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transfer_shouldReturnOk() {
        when(accountService.transferBetweenAccounts(anyLong(), anyLong(), any(BigDecimal.class)))
                .thenReturn(new TransactionDto());
        ResponseEntity<TransactionDto> response = accountController.transfer(1L, 2L, BigDecimal.TEN);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
