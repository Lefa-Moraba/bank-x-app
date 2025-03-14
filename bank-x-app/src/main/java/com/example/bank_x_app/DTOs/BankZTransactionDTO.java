package com.example.bank_x_app.DTOs;

import com.example.bank_x_app.enums.ReconciliationStatus;
import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BankZTransactionDTO {
    private Long id;
    private Long customerId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String externalReference;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private ReconciliationStatus reconciliationStatus;
    private LocalDateTime createdAt;
}
