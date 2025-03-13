package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.AccountDTO;
import com.example.bank_x_app.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO toAccountDTO(AccountEntity accountEntity) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountEntity.getId());
        accountDTO.setAccountNumber(accountEntity.getAccountNumber());
        accountDTO.setBalance(accountEntity.getBalance());
        accountDTO.setAccountType(accountEntity.getAccountType());
        accountDTO.setCreatedAt(accountEntity.getCreatedAt());
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
