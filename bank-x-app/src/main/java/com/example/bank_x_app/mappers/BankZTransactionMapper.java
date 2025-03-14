package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.BankZTransactionDTO;
import com.example.bank_x_app.entities.BankZTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class BankZTransactionMapper {

    public BankZTransactionDTO toBankZTransactionDTO(BankZTransactionEntity bankZTransactionEntity) {
        BankZTransactionDTO bankZTransactionDTO = new BankZTransactionDTO();
        bankZTransactionDTO.setId(bankZTransactionEntity.getId());
        bankZTransactionDTO.setCustomerId(bankZTransactionEntity.getCustomer().getId());
        bankZTransactionDTO.setFromAccountNumber(bankZTransactionEntity.getFromAccount() != null ? bankZTransactionEntity.getFromAccount().getAccountNumber() : null);
        bankZTransactionDTO.setToAccountNumber(bankZTransactionEntity.getToAccount() != null ? bankZTransactionEntity.getToAccount().getAccountNumber() : null);
        bankZTransactionDTO.setExternalReference(bankZTransactionEntity.getExternalReference());
        bankZTransactionDTO.setAmount(bankZTransactionEntity.getAmount());
        bankZTransactionDTO.setTransactionType(bankZTransactionEntity.getTransactionType());
        bankZTransactionDTO.setStatus(bankZTransactionEntity.getStatus());
        bankZTransactionDTO.setReconciliationStatus(bankZTransactionEntity.getReconciliationStatus());
        bankZTransactionDTO.setCreatedAt(bankZTransactionEntity.getCreatedAt());
        return bankZTransactionDTO;
    }

    public BankZTransactionEntity toBankZTransactionEntity(BankZTransactionDTO bankZTransactionDTO) {
        BankZTransactionEntity bankZTransactionEntity = new BankZTransactionEntity();
        bankZTransactionEntity.setAmount(bankZTransactionDTO.getAmount());
        bankZTransactionEntity.setTransactionType(bankZTransactionDTO.getTransactionType());
        bankZTransactionEntity.setExternalReference(bankZTransactionDTO.getExternalReference());
        bankZTransactionEntity.setStatus(bankZTransactionDTO.getStatus());
        bankZTransactionEntity.setReconciliationStatus(bankZTransactionDTO.getReconciliationStatus());
        return bankZTransactionEntity;
    }
}
