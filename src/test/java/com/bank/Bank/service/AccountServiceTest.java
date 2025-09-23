package com.bank.Bank.service;

import com.bank.Bank.dto.AccountDto;
import com.bank.Bank.dto.CreateAccountRequest;
import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Account;
import com.bank.Bank.entity.Customer;
import com.bank.Bank.entity.Transaction;
import com.bank.Bank.entity.TransactionType;
import com.bank.Bank.exception.ResourceNotFoundException;
import com.bank.Bank.exception.WrongInputException;
import com.bank.Bank.mapper.AccountMapper;
import com.bank.Bank.mapper.TransactionMapper;
import com.bank.Bank.repository.AccountRepository;
import com.bank.Bank.repository.CustomerRepository;
import com.bank.Bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccountForCustomer_shouldReturnAccountDto() {
        Customer customer = new Customer();
        CreateAccountRequest req = new CreateAccountRequest();
        req.setInitialBalance(BigDecimal.TEN);
        Account account = new Account();
        AccountDto dto = new AccountDto();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(dto);
        assertNotNull(accountService.createAccountForCustomer(1L, req));
    }

    @Test
    void createAccountForCustomer_shouldThrowIfCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> accountService.createAccountForCustomer(1L, new CreateAccountRequest()));
    }

    @Test
    void depositToCustomerAccount_shouldThrowOnInvalidAmount() {
        assertThrows(WrongInputException.class, () -> accountService.depositToCustomerAccount(1L, BigDecimal.ZERO));
    }

    // Add more tests for deposit, withdraw, and transfer as needed
}
