package com.bank.Bank.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
        public final CustomerService customerService;
        public final AccountService accountService;
        private final CustomerMapper customerMapper;

        @Operation(summary = "Create Customer", description = "Create a customer", tags = { "Customer Register" })
        @ApiResponses({
                        @ApiResponse(description = "201 CREATED", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "201")
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

        @Operation(summary = "Customer Login", description = "Login customer using name and Aadhar", tags = {
                        "Customer Login" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200"),
                        @ApiResponse(description = "401 UNAUTHORIZED", responseCode = "401")
        })
        @PostMapping("/login")
        public ResponseEntity<CustomerDto> login(@RequestBody CreateCustomerRequest request) {
                Optional<CustomerDto> customer = customerService.login(request.getCustomerName(),
                                request.getCustomerAadhar());
                if (customer.isPresent()) {
                        return ResponseEntity.ok(customer.get());
                } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
        }

        @Operation(summary = "Customer Logout", description = "Logout customer", tags = { "Customer Logout" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", responseCode = "200"),
                        @ApiResponse(description = "404 NOT FOUND", responseCode = "404")
        })
        @PostMapping("/{customerId}/logout")
        public ResponseEntity<String> logout(@PathVariable("customerId") Long customerId) {
                boolean loggedOut = customerService.logout(customerId);
                if (loggedOut) {
                        return ResponseEntity.ok("Customer logged out successfully");
                } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
                }
        }

        @Operation(summary = "Get all Customer", description = "Find all customers", tags = { "Get all Customer" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200")
        })
        @GetMapping
        public ResponseEntity<List<CustomerDto>> getAllCustomer() {
                List<CustomerDto> customers = customerService.getAllCustomer();
                return ResponseEntity.ok(customers);
        }

        @Operation(summary = "Get Customer By Customer ID", description = "Get customer by customer ID", tags = {
                        "Get customer by customer ID" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200")
        })

        @GetMapping("/{customerId}")
        public ResponseEntity<CustomerDto> getCustomerById(
                        @PathVariable("customerId") Long customerId) {
                Optional<CustomerDto> customer = customerService.getCustomerById(customerId);
                return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Create Account Customer", description = "Create a customer account", tags = {
                        "Customer Account Create" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200")
        })
        @PostMapping("/{customerId}/createAccount")
        public ResponseEntity<AccountDto> createAccountForCustomer(
                        @PathVariable("customerId") Long customerId,
                        @RequestBody CreateAccountRequest request) {
                AccountDto dto = accountService.createAccountForCustomer(customerId, request);
                return ResponseEntity.ok(dto);
        }

        @Operation(summary = "Deposit to Customer", description = "Deposit to customer's specific account or first account if no account number specified", tags = {
                        "Deposit" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200")
        })
        @PostMapping("/{customerId}/deposit")
        public ResponseEntity<TransactionDto> depositToCustomer(
                        @PathVariable("customerId") Long customerId,
                        @RequestParam("amount") BigDecimal amount,
                        @RequestParam(value = "accountNumber", required = false) Long accountNumber) {
                TransactionDto dto = accountService.depositToCustomerAccount(customerId, amount, accountNumber);
                return ResponseEntity.ok(dto);
        }

        @Operation(summary = "Withdraw from Customer", description = "Withdraw from customer's specific account or first account if no account number specified", tags = {
                        "Withdrawal" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Customer.class)) }, responseCode = "200")
        })
        @PostMapping("/{customerId}/withdraw")
        public ResponseEntity<TransactionDto> withdrawFromCustomer(
                        @PathVariable("customerId") Long customerId,
                        @RequestParam("amount") BigDecimal amount,
                        @RequestParam(value = "accountNumber", required = false) Long accountNumber) {
                TransactionDto dto = accountService.withdrawFromCustomerAccount(customerId, amount, accountNumber);
                return ResponseEntity.ok(dto);
        }

        @Operation(summary = "Get Account Balance", description = "Get balance for a specific account of a customer", tags = {
                        "Account Balance" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = BigDecimal.class)) }, responseCode = "200")
        })
        @GetMapping("/{customerId}/balance")
        public ResponseEntity<BigDecimal> getAccountBalance(
                        @PathVariable("customerId") Long customerId,
                        @RequestParam("accountNumber") Long accountNumber) {
                BigDecimal balance = accountService.getAccountBalance(customerId, accountNumber);
                return ResponseEntity.ok(balance);
        }

}
