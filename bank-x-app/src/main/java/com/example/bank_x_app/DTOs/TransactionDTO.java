package com.example.bank_x_app.DTOs;

import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String externalReference;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}
