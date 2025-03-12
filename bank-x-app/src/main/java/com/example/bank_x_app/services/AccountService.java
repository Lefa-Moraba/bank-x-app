package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.AccountDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.mapper.AccountMapper;
import com.example.bank_x_app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        List<AccountEntity> accountEntities = accountRepository.findByCustomerId(customerId);
        return accountEntities.stream().map(accountMapper::toAccountDTO).collect(Collectors.toList());
    }
}
