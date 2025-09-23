package com.bank.Bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    private String customerName;
    private String customerAddress;
    private String customerAadhar;
    private int customerAge;
}
