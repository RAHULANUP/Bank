package com.bank.Bank.mapper;

import org.springframework.stereotype.Component;

import com.bank.Bank.dto.CustomerDto;
import com.bank.Bank.entity.Customer;

@Component
public class CustomerMapper {
    private final AccountMapper accountMapper;

    public CustomerMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public Customer toEntity(CustomerDto customerDto) {
        if (customerDto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setAccountNumber(customerDto.getAccountNumber());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setCustomerAddress(customerDto.getCustomerAddress());
        customer.setCustomerAadhar(customerDto.getCustomerAadhar());
        customer.setCustomerAge(customerDto.getCustomerAge());

        return customer;
    }

    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setAccountNumber(customer.getAccountNumber());
        customerDto.setCustomerName(customer.getCustomerName());
        customerDto.setCustomerAddress(customer.getCustomerAddress());
        customerDto.setCustomerAadhar(customer.getCustomerAadhar());
        customerDto.setCustomerAge(customer.getCustomerAge());
        if (customer.getAccount() != null) {
            customerDto.setAccounts(customer.getAccount().stream().map(accountMapper::toDto).toList());
        }

        return customerDto;
    }
}
