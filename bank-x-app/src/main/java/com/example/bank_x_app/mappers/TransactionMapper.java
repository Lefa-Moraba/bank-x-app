package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.TransactionDTO;
import com.example.bank_x_app.entities.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toTransactionDTO(TransactionEntity transactionEntity) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transactionEntity.getId());
        transactionDTO.setFromAccountNumber(transactionEntity.getFromAccount() != null ? transactionEntity.getFromAccount().getAccountNumber() : null);
        transactionDTO.setToAccountNumber(transactionEntity.getToAccount() != null ? transactionEntity.getToAccount().getAccountNumber() : null);
        transactionDTO.setAmount(transactionEntity.getAmount());
        transactionDTO.setTransactionType(transactionEntity.getTransactionType());
        transactionDTO.setExternalReference(transactionEntity.getExternalReference());
        transactionDTO.setStatus(transactionEntity.getStatus());
        transactionDTO.setCreatedAt(transactionEntity.getCreatedAt());
        return transactionDTO;
    }

    public TransactionEntity toTransactionEntity(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setTransactionType(transactionDTO.getTransactionType());
        transactionEntity.setExternalReference(transactionDTO.getExternalReference());
        transactionEntity.setStatus(transactionDTO.getStatus());
        return transactionEntity;
    }
}
