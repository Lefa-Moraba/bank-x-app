package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.TransactionDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.entities.TransactionEntity;
import com.example.bank_x_app.enums.AccountType;
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

    private BigDecimal interestRate = new BigDecimal("0.005");
    private BigDecimal chargePercentage = new BigDecimal("0.0005");

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        switch (transactionDTO.getTransactionType()) {
            case DEPOSIT:
                return processDeposit(transactionDTO);
            case TRANSFER:
                return processTransfer(transactionDTO);
            case INTERNAL_TRANSFER:
                return processInternalTransfer(transactionDTO);
            default:
                throw new RuntimeException("Unsupported transaction type: " + transactionDTO.getTransactionType());
        }
    }

    private TransactionDTO processDeposit(TransactionDTO transactionDTO) {
        AccountEntity toAccount = accountRepository.findByAccountNumber(transactionDTO.getToAccountNumber());
        if (toAccount == null) {
            throw new RuntimeException(String.format("Account %s not found.", transactionDTO.getToAccountNumber()));
        }

        toAccount.setBalance(toAccount.getBalance().add(transactionDTO.getAmount()));
        accountRepository.save(toAccount);

        TransactionEntity depositTransaction = createTransactionEntity(null, toAccount, transactionDTO.getAmount(), TransactionType.DEPOSIT);
        TransactionDTO savedTransactionDTO = transactionMapper.toTransactionDTO(transactionRepository.save(depositTransaction));

        // If the deposit is into a Savings Account, generate an interest transaction
        if ((AccountType.SAVINGS).equals(toAccount.getAccountType())) {
            createInterestTransaction(toAccount);
        }

        return savedTransactionDTO;
    }

    private TransactionDTO processTransfer(TransactionDTO transactionDTO) {
        AccountEntity fromAccount = accountRepository.findByAccountNumber(transactionDTO.getFromAccountNumber());
        if (fromAccount == null) {
            throw new RuntimeException(String.format("Account %s not found.", transactionDTO.getFromAccountNumber()));
        }

        AccountEntity toAccount = accountRepository.findByAccountNumber(transactionDTO.getToAccountNumber());
        if (toAccount == null) {
            throw new RuntimeException(String.format("Account %s not found.", transactionDTO.getToAccountNumber()));
        }

        if (!(AccountType.CURRENT).equals(fromAccount.getAccountType())) {
            throw new RuntimeException("Only Current Accounts can perform Transfers.");
        }

        if (fromAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds for transfer.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transactionDTO.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transactionDTO.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransactionEntity transferTransaction = createTransactionEntity(fromAccount, toAccount, transactionDTO.getAmount(), TransactionType.TRANSFER);
        TransactionDTO savedTransactionDTO = transactionMapper.toTransactionDTO(transactionRepository.save(transferTransaction));

        // Generate Charges transaction to BankX
        createChargesTransaction(fromAccount, transactionDTO.getAmount());

        return savedTransactionDTO;
    }

    private TransactionDTO processInternalTransfer(TransactionDTO transactionDTO) {

        AccountEntity fromAccount = accountRepository.findByAccountNumber(transactionDTO.getFromAccountNumber());
        if (fromAccount == null) {
            throw new RuntimeException(String.format("Account %s not found.", transactionDTO.getFromAccountNumber()));
        }

        AccountEntity toAccount = accountRepository.findByAccountNumber(transactionDTO.getToAccountNumber());
        if (toAccount == null) {
            throw new RuntimeException(String.format("Account %s not found.", transactionDTO.getToAccountNumber()));
        }


        if (fromAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds for internal transfer.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transactionDTO.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transactionDTO.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransactionEntity internalTransferTransaction = createTransactionEntity(fromAccount, toAccount, transactionDTO.getAmount(), TransactionType.INTERNAL_TRANSFER);
        return transactionMapper.toTransactionDTO(transactionRepository.save(internalTransferTransaction));
    }

    private void createInterestTransaction(AccountEntity savingsAccount) {

        BigDecimal interestAmount = savingsAccount.getBalance().multiply(interestRate);

        TransactionEntity interestTransaction = createTransactionEntity(null, savingsAccount, interestAmount, TransactionType.INTEREST);
        transactionRepository.save(interestTransaction);

        savingsAccount.setBalance(savingsAccount.getBalance().add(interestAmount));
        accountRepository.save(savingsAccount);
    }

    private void createChargesTransaction(AccountEntity currentAccount, BigDecimal transactionAmount) {

        BigDecimal chargeAmount = transactionAmount.multiply(chargePercentage);

        if (currentAccount.getBalance().compareTo(chargeAmount) < 0) {
            throw new RuntimeException("Insufficient funds for charges.");
        }

        currentAccount.setBalance(currentAccount.getBalance().subtract(chargeAmount));
        accountRepository.save(currentAccount);

        TransactionEntity chargesTransaction = createTransactionEntity(currentAccount, null, chargeAmount, TransactionType.CHARGES);
        transactionRepository.save(chargesTransaction);
    }

    private TransactionEntity createTransactionEntity(AccountEntity fromAccount, AccountEntity toAccount, BigDecimal amount, TransactionType type) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setExternalReference(generateExternalReference());
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
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
