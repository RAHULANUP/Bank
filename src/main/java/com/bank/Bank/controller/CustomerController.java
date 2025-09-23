package com.bank.Bank.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.Bank.dto.AccountDto;
import com.bank.Bank.dto.CreateAccountRequest;
import com.bank.Bank.dto.CreateCustomerRequest;
import com.bank.Bank.dto.CustomerDto;
import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Customer;
import com.bank.Bank.mapper.CustomerMapper;
import com.bank.Bank.service.AccountService;
import com.bank.Bank.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    public final CustomerService customerService;
    public final AccountService accountService;
    private final CustomerMapper customerMapper;
    @Operation(
        summary="Create Customer",
        description="Create a customer",
        tags={"Customer Register"}
    )
    @ApiResponses({
    @ApiResponse(
        description="201 CREATED",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="201")
    })
    @PostMapping
    public ResponseEntity<CustomerDto> register(@RequestBody CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerAadhar(request.getCustomerAadhar());
        customer.setCustomerAddress(request.getCustomerAddress());
        customer.setCustomerAge(request.getCustomerAge());

        CustomerDto createdCustomer = customerService.register(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }
    @Operation(
        summary="Get all Customer",
        description="Find all customers",
        tags={"Get all Customer"}
    )
    @ApiResponses({
    @ApiResponse(
        description="200 OK",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="200")
    })   
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomer() {
        List<CustomerDto> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }
    @Operation(
        summary="Get Customer By Account number",
        description="Get cusotmer by account number",
        tags={"Get customer by account number"}
    )
    @ApiResponses({
    @ApiResponse(
        description="200 OK",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="200")
    })

    @GetMapping("/{customerAccountNumber}")
    public ResponseEntity<CustomerDto> getCustomerByAccountNumber(
            @PathVariable("customerAccountNumber") Long customerAccountNumber) {
        Optional<CustomerDto> customer = customerService.getCustomerByAccountNumber(customerAccountNumber);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(
        summary="Create Account Customer",
        description="Create a customer account",
        tags={"Customer Account Create"}
    )
    @ApiResponses({
    @ApiResponse(
        description="200 OK",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="200")
    })
    @PostMapping("/{customerAccountNumber}/createAccount")
    public ResponseEntity<AccountDto> createAccountForCustomer(
            @PathVariable("customerAccountNumber") Long customerAccountNumber,
            @RequestBody CreateAccountRequest request) {
        AccountDto dto = accountService.createAccountForCustomer(customerAccountNumber, request);
        return ResponseEntity.ok(dto);
    }
    @Operation(
        summary="Deposit to Customer",
        description="Deposit to customer",
        tags={"Deposit"}
    )
    @ApiResponses({
    @ApiResponse(
        description="200 OK",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="200")
    })
    @PostMapping("/{customerAccountNumber}/deposit")
    public ResponseEntity<TransactionDto> depositToCustomer(
            @PathVariable("customerAccountNumber") Long customerAccountNumber,
            @RequestParam("amount") BigDecimal amount) {
        TransactionDto dto = accountService.depositToCustomerAccount(customerAccountNumber, amount);
        return ResponseEntity.ok(dto);
    }
    @Operation(
        summary="Withdraw from Customer",
        description="Withdraw from cusotmer",
        tags={"Withdrawal"}
    )
    @ApiResponses({
    @ApiResponse(
        description="200 OK",
        content={@Content(schema=@Schema(implementation=Customer.class))},
        responseCode="200")
    })
    @PostMapping("/{customerAccountNumber}/withdraw")
    public ResponseEntity<TransactionDto> withdrawFromCustomer(
            @PathVariable("customerAccountNumber") Long customerAccountNumber,
            @RequestParam("amount") BigDecimal amount) {
        TransactionDto dto = accountService.withdrawFromCustomerAccount(customerAccountNumber, amount);
        return ResponseEntity.ok(dto);
    }

}
