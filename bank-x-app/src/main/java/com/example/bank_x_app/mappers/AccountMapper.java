package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.AccountDTO;
import com.example.bank_x_app.entities.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final TransactionMapper transactionMapper;

    public AccountDTO toAccountDTO(AccountEntity accountEntity) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountEntity.getId());
        accountDTO.setAccountNumber(accountEntity.getAccountNumber());
        accountDTO.setBalance(accountEntity.getBalance());
        accountDTO.setAccountType(accountEntity.getAccountType());
        accountDTO.setCreatedAt(accountEntity.getCreatedAt());

        if (accountEntity.getOutgoingTransactions() != null || accountEntity.getIncomingTransactions() != null) {
            accountDTO.setTransactions(accountEntity.getOutgoingTransactions().stream()
                    .map(transactionMapper::toTransactionDTO)
                    .collect(Collectors.toList()));

            accountDTO.getTransactions().addAll(accountEntity.getIncomingTransactions().stream()
                    .map(transactionMapper::toTransactionDTO)
                    .collect(Collectors.toList()));
        }

        return accountDTO;
    }

    public AccountEntity toAccountEntity(AccountDTO accountDTO) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountDTO.getId());
        accountEntity.setAccountNumber(accountDTO.getAccountNumber());
        accountEntity.setBalance(accountDTO.getBalance());
        accountEntity.setAccountType(accountDTO.getAccountType());
        accountEntity.setCreatedAt(accountDTO.getCreatedAt());
        return accountEntity;
    }
}
