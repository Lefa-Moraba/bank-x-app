package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.entities.CustomerEntity;
import com.example.bank_x_app.entities.TransactionEntity;
import com.example.bank_x_app.enums.AccountType;
import com.example.bank_x_app.enums.TransactionStatus;
import com.example.bank_x_app.enums.TransactionType;
import com.example.bank_x_app.mappers.AccountMapper;
import com.example.bank_x_app.mappers.CustomerMapper;
import com.example.bank_x_app.repositories.AccountRepository;
import com.example.bank_x_app.repositories.CustomerRepository;
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
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerMapper customerMapper;

    private BigDecimal joiningBonusAmount = new BigDecimal(500);

    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        return customerEntities.stream().map(customerMapper::toCustomerDTO).collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        Optional<CustomerEntity> customerEntity = customerRepository.findByEmail(email);
        return  customerEntity.map(customerMapper::toCustomerDTO);
    }

    @Transactional
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {

        CustomerEntity customerEntity = customerMapper.toCustomerEntity(customerDTO);
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);

        AccountEntity currentAccount = createAccount(savedCustomer, AccountType.CURRENT, BigDecimal.ZERO);
        AccountEntity savingsAccount = createAccount(savedCustomer, AccountType.SAVINGS, joiningBonusAmount);

        createJoiningBonusTransaction(savingsAccount, joiningBonusAmount);

        return customerMapper.toCustomerDTO(savedCustomer);
    }


    private AccountEntity createAccount(CustomerEntity customerEntity, AccountType accountType, BigDecimal initialBalance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCustomer(customerEntity);
        accountEntity.setAccountType(accountType);
        accountEntity.setBalance(initialBalance);
        accountEntity.setAccountNumber(generateAccountNumber());

        return accountRepository.save(accountEntity);
    }

    private void createJoiningBonusTransaction(AccountEntity savingsAccount, BigDecimal amount) {
        TransactionEntity joiningBonusTransaction = new TransactionEntity();
        joiningBonusTransaction.setFromAccount(null);
        joiningBonusTransaction.setToAccount(savingsAccount);
        joiningBonusTransaction.setAmount(amount);
        joiningBonusTransaction.setTransactionType(TransactionType.DEPOSIT);
        joiningBonusTransaction.setExternalReference(generateExternalReference());
        joiningBonusTransaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.save(joiningBonusTransaction);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20); // Unique 10-digit account number
    }

    private String generateExternalReference() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }
}

