package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.AccountEntity;
import com.example.bank_x_app.entities.CustomerEntity;
import com.example.bank_x_app.enums.AccountType;
import com.example.bank_x_app.mappers.AccountMapper;
import com.example.bank_x_app.mappers.CustomerMapper;
import com.example.bank_x_app.repositories.AccountRepository;
import com.example.bank_x_app.repositories.CustomerRepository;
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
    private final CustomerMapper customerMapper;
    private final AccountMapper accountMapper;

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

        createAccount(savedCustomer, AccountType.CURRENT, BigDecimal.ZERO);
        createAccount(savedCustomer, AccountType.SAVINGS, BigDecimal.ZERO);

        return customerMapper.toCustomerDTO(savedCustomer);
    }


    private void createAccount(CustomerEntity customerEntity, AccountType accountType, BigDecimal initialBalance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCustomer(customerEntity);
        accountEntity.setAccountType(accountType);
        accountEntity.setBalance(initialBalance);
        accountEntity.setAccountNumber(generateAccountNumber());

        accountRepository.save(accountEntity);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20); // Unique 10-digit account number
    }


}

