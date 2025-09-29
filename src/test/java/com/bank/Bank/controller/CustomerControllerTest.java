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

class CustomerControllerTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void withdrawFromCustomer_shouldReturnOk() {
        when(accountService.withdrawFromCustomerAccount(anyLong(), any(BigDecimal.class), null))
                .thenReturn(new TransactionDto());
        ResponseEntity<TransactionDto> response = customerController.withdrawFromCustomer(1L, BigDecimal.TEN, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void depositToCustomer_shouldReturnOk() {
        when(accountService.depositToCustomerAccount(anyLong(), any(BigDecimal.class), any()))
                .thenReturn(new TransactionDto());
        ResponseEntity<TransactionDto> response = customerController.depositToCustomer(1L, BigDecimal.TEN, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
}
