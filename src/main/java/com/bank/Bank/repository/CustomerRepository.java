package com.bank.Bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.Bank.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.account WHERE c.accountNumber = :accountNumber")
    Optional<Customer> findByIdAccounts(@Param("accountNumber") Long accountNumber);
}
