package com.bank.Bank.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.entity.Transaction;
import com.bank.Bank.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Get Transaction By account ID", description = "Get the transaction by account id", tags = {
            "Get all transaction of an account" })
    @ApiResponses({
            @ApiResponse(description = "201 CREATED", content = {
                    @Content(schema = @Schema(implementation = Transaction.class)) }, responseCode = "201")
    })
    @GetMapping("/by-account")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccount(@RequestParam Long accountNumber) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}
