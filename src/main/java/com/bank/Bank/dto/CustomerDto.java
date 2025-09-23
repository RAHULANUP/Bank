package com.bank.Bank.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long accountNumber;
    private String customerName;
    private String customerAddress;
    private String customerAadhar;
    private int customerAge;
    private List<AccountDto> accounts;
}
