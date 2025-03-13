package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.TransactionDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.entities.TransactionEntity;
import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import com.example.bank_x_app.mappers.TransactionMapper;
import com.example.bank_x_app.repositories.AccountRepository;
import com.example.bank_x_app.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(transactionDTO);

        if (transactionDTO.getTransactionType() == TransactionType.TRANSFER) {
            AccountEntity fromAccount = accountRepository.findByAccountNumber(transactionDTO.getFromAccountNumber());

            AccountEntity toAccount = accountRepository.findByAccountNumber(transactionDTO.getToAccountNumber());

            if (fromAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(transactionDTO.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transactionDTO.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transactionEntity.setFromAccount(fromAccount);
            transactionEntity.setToAccount(toAccount);
        }

        transactionEntity.setExternalReference(generateExternalReference());
        transactionEntity.setStatus(TransactionStatus.COMPLETED);

        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);
        return transactionMapper.toTransactionDTO(savedTransaction);
    }

    public List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumber(accountNumber, accountNumber)
                .stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public Optional<TransactionDTO> getTransactionByReference(String externalReference) {
        return transactionRepository.findByExternalReference(externalReference)
                .map(transactionMapper::toTransactionDTO);
    }

    private String generateExternalReference() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }
}
