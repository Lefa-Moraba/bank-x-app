package com.example.bank_x_app.controllers;

import com.example.bank_x_app.DTOs.BankZTransactionDTO;
import com.example.bank_x_app.services.BankZTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bankz")
@RequiredArgsConstructor
public class BankZTransactionController {

    private final BankZTransactionService bankZTransactionService;

    @PostMapping("/transaction")
    public ResponseEntity<BankZTransactionDTO> processImmediateTransaction(@RequestBody BankZTransactionDTO bankZTransactionDTO) {
        return ResponseEntity.ok(bankZTransactionService.processImmediate(bankZTransactionDTO));
    }

    @PostMapping("/transactions/batch")
    public ResponseEntity<List<BankZTransactionDTO>> processBatchTransactions(@RequestBody List<BankZTransactionDTO> bankZTransactionDTOList) {
        return ResponseEntity.ok(bankZTransactionService.processBatchImmediate(bankZTransactionDTOList));
    }

    @PostMapping("/reconciliation")
    public ResponseEntity<Map<String, Object>> reconcileTransactions(@RequestBody List<BankZTransactionDTO> bankZTransactionDTOList) {
        return ResponseEntity.ok(bankZTransactionService.reconcileTransactions(bankZTransactionDTOList));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<BankZTransactionDTO>> getAllBankZTransactions() {
        return ResponseEntity.ok(bankZTransactionService.getAllBankZTransactions());
    }

    @GetMapping("/transactions/unreconciled")
    public ResponseEntity<List<BankZTransactionDTO>> getUnreconciledTransactions() {
        return ResponseEntity.ok(bankZTransactionService.getUnreconciledTransactions());
    }
}