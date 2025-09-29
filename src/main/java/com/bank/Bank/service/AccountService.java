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
    public TransactionDto transferBetweenAccounts(Long fromAccountNumber, Long toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new WrongInputException("Source and destination accounts must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Transfer amount must be greater than zero");
        }
        Account fromAccount = accountRepository.findById(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + fromAccountNumber));
        Account toAccount = accountRepository.findById(toAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found: " + toAccountNumber));
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

    public AccountDto createAccountForCustomer(Long customerId, CreateAccountRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        Account account = new Account();
        account.setBalance(request.getInitialBalance());
        account.setCustomer(customer);
        Account saved = accountRepository.save(account);
        return accountMapper.toDto(saved);
    }

    public AccountDto getFirstAccountInfo(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id:" + customerId));

        if (customer.getAccount() == null || customer.getAccount().isEmpty()) {
            throw new ResourceNotFoundException("No account found for customer id: " + customerId);
        }

        return accountMapper.toDto(customer.getAccount().get(0));
    }

    @Transactional
    public TransactionDto depositToCustomerAccount(Long customerId, BigDecimal amount, Long accountNumber) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Deposit amount must be greater than zero");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        if (customer.getAccount() == null || customer.getAccount().isEmpty()) {
            throw new ResourceNotFoundException("No account found for customer id: " + customerId);
        }

        Account account;
        if (accountNumber != null) {
            // Find the specific account by account number
            account = customer.getAccount().stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Account number " + accountNumber + " not found for customer id: " + customerId));
        } else {
            // Use the first account if no account number is specified
            account = customer.getAccount().get(0);
        }

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

    public TransactionDto withdrawFromCustomerAccount(Long customerId, BigDecimal amount, Long accountNumber) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WrongInputException("Withdrawal amount must be greater than zero");
        }
        Customer customer = customerRepository.findByIdAccounts(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        if (customer.getAccount() == null || customer.getAccount().isEmpty()) {
            throw new ResourceNotFoundException("No account found for customer id: " + customerId);
        }

        Account account;
        if (accountNumber != null) {
            // Find the specific account by account number
            account = customer.getAccount().stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Account number " + accountNumber + " not found for customer id: " + customerId));
        } else {
            // Use the first account if no account number is specified
            account = customer.getAccount().get(0);
        }

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

    public BigDecimal getAccountBalance(Long customerId, Long accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

        // Verify that the account belongs to the customer
        if (!account.getCustomer().getCustomerId().equals(customerId)) {
            throw new WrongInputException("Account does not belong to the specified customer");
        }

        return account.getBalance();
    }
}
