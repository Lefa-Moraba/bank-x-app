package com.example.bank_x_app.services;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.CustomerEntity;
import com.example.bank_x_app.mapper.CustomerMapper;
import com.example.bank_x_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        return customerEntities.stream().map(customerMapper::toCustomerDTO).collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        Optional<CustomerEntity> customerEntitie = customerRepository.findByEmail(email);
        return  customerEntitie.map(customerMapper::toCustomerDTO);
    }

}

