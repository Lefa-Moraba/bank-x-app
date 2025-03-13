package com.example.bank_x_app.repositories;

import com.example.bank_x_app.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByFromAccount_AccountNumberOrToAccount_AccountNumber(String fromAccountNumber, String toAccountNumber);
    Optional<TransactionEntity> findByExternalReference(String externalReference);
}
