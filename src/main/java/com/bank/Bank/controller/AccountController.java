package com.bank.Bank.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.Bank.dto.AccountDto;
import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Account;
import com.bank.Bank.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
        private final AccountService accountService;

        @Operation(summary = "Get the account information", description = "Finds the first account information", tags = {
                        "Get account info" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Account.class)) }, responseCode = "200")
        })
        @GetMapping("/{customerId}")
        public ResponseEntity<AccountDto> getFirstAccountInfo(@PathVariable("customerId") Long customerId) {
                AccountDto dto = accountService.getFirstAccountInfo(customerId);
                return ResponseEntity.ok(dto);
        }

        @Operation(summary = "Transfer from one account to another", description = "Transfer from one account to another on the basis of account Id", tags = {
                        "Transfer" })
        @ApiResponses({
                        @ApiResponse(description = "200 OK", content = {
                                        @Content(schema = @Schema(implementation = Account.class)) }, responseCode = "200")
        })
        @PostMapping("/transfer")
        public ResponseEntity<TransactionDto> transfer(
                        @RequestParam Long fromAccountNumber,
                        @RequestParam Long toAccountNumber,
                        @RequestParam BigDecimal amount) {
                TransactionDto dto = accountService.transferBetweenAccounts(fromAccountNumber, toAccountNumber, amount);
                return ResponseEntity.ok(dto);
        }
}
