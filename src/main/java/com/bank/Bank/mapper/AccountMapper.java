package com.bank.Bank.mapper;

import org.springframework.stereotype.Component;

import com.bank.Bank.dto.AccountDto;
import com.bank.Bank.entity.Account;

@Component
public class AccountMapper {
    public Account toEntity(AccountDto accountDto) {
        if (accountDto == null) {
            return null;
        }
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    public AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());

        if (account.getCustomer() != null) {
            accountDto.setCustomerId(account.getCustomer().getAccountNumber());
            accountDto.setCustomerName(account.getCustomer().getCustomerName());
        }
        return accountDto;
    }
}