package com.bank.Bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bank.Bank.dto.CustomerDto;
import com.bank.Bank.entity.Customer;
import com.bank.Bank.mapper.CustomerMapper;
import com.bank.Bank.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    public final CustomerRepository customerRepository;
    public final CustomerMapper customerMapper;

    public CustomerDto register(Customer customer) {
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }

    public List<CustomerDto> getAllCustomer() {
        List<Customer> saved = customerRepository.findAll();
        return saved.stream().map(customerMapper::toDto).toList();
    }

    public Optional<CustomerDto> getCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.map(customerMapper::toDto);
    }

    public Optional<CustomerDto> login(String customerName, String customerAadhar) {
        List<Customer> customers = customerRepository.findAll();
        Optional<Customer> customer = customers.stream()
                .filter(c -> c.getCustomerName().equals(customerName) &&
                        c.getCustomerAadhar().equals(customerAadhar))
                .findFirst();
        return customer.map(customerMapper::toDto);
    }

    public boolean logout(Long customerId) {
        return customerRepository.existsById(customerId);
    }

}
