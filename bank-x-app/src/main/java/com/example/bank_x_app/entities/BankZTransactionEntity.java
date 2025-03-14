package com.example.bank_x_app.entities;

import com.example.bank_x_app.enums.ReconciliationStatus;
import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bankz_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankZTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_number", referencedColumnName = "account_number", nullable = true)
    private AccountEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_number", referencedColumnName = "account_number", nullable = true)
    private AccountEntity toAccount;

    @Column(name = "external_reference", unique = true, nullable = false)
    private String externalReference;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "reconciliation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReconciliationStatus reconciliationStatus = ReconciliationStatus.NOT_RECONCILED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
