package com.example.bank_x_app.controllers;

import com.example.bank_x_app.DTOs.TransactionDTO;
import com.example.bank_x_app.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionDTO));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }

    @GetMapping("/reference/{externalReference}")
    public ResponseEntity<Optional<TransactionDTO>> getTransactionByReference(@PathVariable String externalReference) {
        return ResponseEntity.ok(transactionService.getTransactionByReference(externalReference));
    }
}
