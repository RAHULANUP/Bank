package com.bank.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.Bank.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
