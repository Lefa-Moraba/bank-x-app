package com.example.bank_x_app.DTOs;

import com.example.bank_x_app.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    private CustomerDTO customer;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    private List<TransactionDTO> transactions;
}
