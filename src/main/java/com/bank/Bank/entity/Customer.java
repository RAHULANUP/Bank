package com.bank.Bank.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountNumber;

    @Column(name = "Name", nullable = false)
    private String customerName;

    @Column(name = "Address", nullable = false)
    private String customerAddress;

    @Column(name = "Aadhar_number", unique = true, nullable = false)
    private String customerAadhar;

    @Column(name = "Age", nullable = false)
    private int customerAge;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> account;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Transaction> transaction;
}
