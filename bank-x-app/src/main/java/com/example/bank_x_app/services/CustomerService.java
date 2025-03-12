package com.example.bank_x_app.services;

import com.example.bank_x_app.entities.CustomerEntity;
import com.example.bank_x_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<CustomerEntity> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

}

