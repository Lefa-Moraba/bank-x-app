package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.BankZTransactionDTO;
import com.example.bank_x_app.DTOs.TransactionDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.entities.BankZTransactionEntity;
import com.example.bank_x_app.entities.CustomerEntity;
import com.example.bank_x_app.enums.ReconciliationStatus;
import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import com.example.bank_x_app.mappers.BankZTransactionMapper;
import com.example.bank_x_app.repositories.AccountRepository;
import com.example.bank_x_app.repositories.BankZTransactionRepository;
import com.example.bank_x_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankZTransactionService {

    private final BankZTransactionRepository bankZTransactionRepository;
    private final BankZTransactionMapper bankZTransactionMapper;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public BankZTransactionDTO processImmediate(BankZTransactionDTO bankZTransactionDTO) {

        TransactionDTO transactionDTO = convertToTransactionDTO(bankZTransactionDTO);

        TransactionDTO processedTransaction = transactionService.createTransaction(transactionDTO);

        BankZTransactionEntity bankZTransactionEntity = createBankZTransactionEntity(bankZTransactionDTO, processedTransaction);
        BankZTransactionEntity savedEntity = bankZTransactionRepository.save(bankZTransactionEntity);

        return bankZTransactionMapper.toBankZTransactionDTO(savedEntity);
    }

    @Transactional
    public List<BankZTransactionDTO> processBatchImmediate(List<BankZTransactionDTO> bankZTransactionDTOList) {
        List<BankZTransactionDTO> processedTransactions = new ArrayList<>();

        for (BankZTransactionDTO bankZTransactionDTO : bankZTransactionDTOList) {
            try {
                BankZTransactionDTO processedTransaction = processImmediate(bankZTransactionDTO);
                processedTransactions.add(processedTransaction);
            }
            catch (Exception e) {
                System.err.println("Error processing transaction: " + e.getMessage());

                // Create a failed transaction record
                bankZTransactionDTO.setStatus(TransactionStatus.FAILED);
                BankZTransactionEntity failedEntity = bankZTransactionMapper.toBankZTransactionEntity(bankZTransactionDTO);
                failedEntity.setExternalReference(bankZTransactionDTO.getExternalReference() != null ?
                        bankZTransactionDTO.getExternalReference() : generateExternalReference());

                BankZTransactionEntity savedFailedEntity = bankZTransactionRepository.save(failedEntity);
                processedTransactions.add(bankZTransactionMapper.toBankZTransactionDTO(savedFailedEntity));
            }
        }

        return processedTransactions;
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    public void scheduledReconciliation() {

        List<BankZTransactionDTO> unreconciledTransactions = getUnreconciledTransactions();

        reconcileTransactions(unreconciledTransactions);
    }

    @Transactional
    public Map<String, Object> reconcileTransactions(List<BankZTransactionDTO> bankZTransactionDTOList) {

        List<String> externalReferences = bankZTransactionDTOList.stream()
                .map(BankZTransactionDTO::getExternalReference)
                .collect(Collectors.toList());

        List<BankZTransactionEntity> existingTransactions = bankZTransactionRepository.findByExternalReferenceIn(externalReferences);

        for (BankZTransactionEntity transaction : existingTransactions) {
            transaction.setReconciliationStatus(ReconciliationStatus.RECONCILED);
        }
        bankZTransactionRepository.saveAll(existingTransactions);

        Map<String, Object> result = Map.of(
                "total", bankZTransactionDTOList.size(),
                "totalReconciled", existingTransactions.size(),
                "reconciledTransactions", existingTransactions.stream()
                        .map(bankZTransactionMapper::toBankZTransactionDTO)
                        .collect(Collectors.toList())
        );

        return result;
    }

    private TransactionDTO convertToTransactionDTO(BankZTransactionDTO bankZTransactionDTO) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNumber(bankZTransactionDTO.getFromAccountNumber());
        transactionDTO.setToAccountNumber(bankZTransactionDTO.getToAccountNumber());
        transactionDTO.setAmount(bankZTransactionDTO.getAmount());
        transactionDTO.setTransactionType(bankZTransactionDTO.getTransactionType());
        transactionDTO.setStatus(bankZTransactionDTO.getStatus());
        transactionDTO.setExternalReference(bankZTransactionDTO.getExternalReference());
        transactionDTO.setCreatedAt(bankZTransactionDTO.getCreatedAt());

        return transactionDTO;
    }

    private BankZTransactionEntity createBankZTransactionEntity(BankZTransactionDTO bankZTransactionDTO, TransactionDTO processedTransaction) {
        BankZTransactionEntity entity = new BankZTransactionEntity();

        // Set accounts
        if (bankZTransactionDTO.getFromAccountNumber() != null) {
            entity.setFromAccount(accountRepository.findByAccountNumber(bankZTransactionDTO.getFromAccountNumber()));
        }

        if (bankZTransactionDTO.getToAccountNumber() != null) {
            entity.setToAccount(accountRepository.findByAccountNumber(bankZTransactionDTO.getToAccountNumber()));
        }


        CustomerEntity customer = null;
        if (bankZTransactionDTO.getCustomerId() != null) {
            customer = customerRepository.findById(bankZTransactionDTO.getCustomerId()).orElse(null);
        } else if (entity.getFromAccount() != null) {
            customer = entity.getFromAccount().getCustomer();
        } else if (entity.getToAccount() != null) {
            customer = entity.getToAccount().getCustomer();
        }
        entity.setCustomer(customer);


        entity.setAmount(bankZTransactionDTO.getAmount());
        entity.setTransactionType(processedTransaction.getTransactionType());
        entity.setStatus(TransactionStatus.COMPLETED);
        entity.setExternalReference(processedTransaction.getExternalReference());
        entity.setReconciliationStatus(ReconciliationStatus.NOT_RECONCILED);

        return entity;
    }

    private String generateExternalReference() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }

    public List<BankZTransactionDTO> getAllBankZTransactions() {
        return bankZTransactionRepository.findAll().stream()
                .map(bankZTransactionMapper::toBankZTransactionDTO)
                .collect(Collectors.toList());
    }

    public List<BankZTransactionDTO> getUnreconciledTransactions() {
        return bankZTransactionRepository.findByReconciliationStatus(ReconciliationStatus.NOT_RECONCILED).stream()
                .map(bankZTransactionMapper::toBankZTransactionDTO)
                .collect(Collectors.toList());
    }
}