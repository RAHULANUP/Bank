package com.bank.Bank.controller;

import com.bank.Bank.dto.TransactionDto;
import com.bank.Bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void getTransactionsByAccount_shouldReturnOk() throws Exception {
        Mockito.when(transactionService.getTransactionsByAccountId(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(new TransactionDto()));

        mockMvc.perform(get("/api/transaction/by-account")
                .param("accountId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
