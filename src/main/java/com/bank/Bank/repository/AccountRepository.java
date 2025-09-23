package com.bank.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.Bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    
}
