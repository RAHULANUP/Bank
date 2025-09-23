package com.bank.Bank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.Bank.exception.ResourceNotFoundException;
import com.bank.Bank.exception.WrongInputException;

import org.springframework.stereotype.Service;

import com.bank.Bank.dto.AccountDto;
import com.bank.Bank.dto.CreateAccountRequest;
import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Account;
import com.bank.Bank.entity.Customer;
import com.bank.Bank.entity.Transaction;
import com.bank.Bank.entity.TransactionType;
import com.bank.Bank.mapper.AccountMapper;
import com.bank.Bank.mapper.TransactionMapper;
import com.bank.Bank.repository.AccountRepository;
import com.bank.Bank.repository.CustomerRepository;
import com.bank.Bank.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Transactional
    public TransactionDto transferBetweenAccounts(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new WrongInputException("Source and destination accounts must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Transfer amount must be greater than zero");
        }
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found: " + toAccountId));
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new WrongInputException("Insufficient balance in source account");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        // Record transaction for sender (WITHDRAWAL)
        Transaction fromTx = new Transaction();
        fromTx.setAmount(amount);
        fromTx.setType(TransactionType.WITHDRAWAL);
        fromTx.setTransactionDate(LocalDateTime.now());
        fromTx.setCustomer(fromAccount.getCustomer());
        fromTx.setAccount(fromAccount);
        transactionRepository.save(fromTx);
        // Record transaction for receiver (DEPOSIT)
        Transaction toTx = new Transaction();
        toTx.setAmount(amount);
        toTx.setType(TransactionType.DEPOSIT);
        toTx.setTransactionDate(LocalDateTime.now());
        toTx.setCustomer(toAccount.getCustomer());
        toTx.setAccount(toAccount);
        transactionRepository.save(toTx);
        // Return the sender's transaction as response
        return transactionMapper.toDto(fromTx);
    }

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    public AccountDto createAccountForCustomer(Long customerAccountNumber, CreateAccountRequest request) {
        Customer customer = customerRepository.findById(customerAccountNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerAccountNumber));
        Account account = new Account();
        account.setBalance(request.getInitialBalance());
        account.setCustomer(customer);
        Account saved = accountRepository.save(account);
        return accountMapper.toDto(saved);
    }

    public AccountDto getFirstAccountInfo(Long customerAccountNumber) {
        Customer customer = customerRepository.findById(customerAccountNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id:" + customerAccountNumber));

        return accountMapper.toDto(customer.getAccount().get(0));
    }

    @Transactional
    public TransactionDto depositToCustomerAccount(Long customerAccountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Deposit amount must be greater than zero");
        }
        Customer customer = customerRepository.findById(customerAccountNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerAccountNumber));
        if (customer.getAccount() == null || customer.getAccount().isEmpty()) {
            throw new ResourceNotFoundException("No account found for customer id: " + customerAccountNumber);
        }
        Account account = customer.getAccount().get(0);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCustomer(account.getCustomer());
        transaction.setAccount(account);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public TransactionDto withdrawFromCustomerAccount(Long customerAccountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Withdrawal amount must be greater than zero");
        }
        Customer customer = customerRepository.findByIdAccounts(customerAccountNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerAccountNumber));
        if (customer.getAccount() == null || customer.getAccount().isEmpty()) {
            throw new ResourceNotFoundException("No account found for customer id: " + customerAccountNumber);
        }
        Account account = customer.getAccount().get(0);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new WrongInputException("Insufficient balance for withdrawal");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCustomer(customer);
        transaction.setAccount(account);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }
}
