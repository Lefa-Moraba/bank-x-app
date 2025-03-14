package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AccountMapper accountMapper;

    public CustomerDTO toCustomerDTO(CustomerEntity customerEntity) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerEntity.getId());
        customerDTO.setFirstName(customerEntity.getFirstName());
        customerDTO.setLastName(customerEntity.getLastName());
        customerDTO.setEmail(customerEntity.getEmail());
        customerDTO.setPhone(customerEntity.getPhone());
        customerDTO.setDateOfBirth(customerEntity.getDateOfBirth());

        if (customerEntity.getAccounts() != null) {
            customerDTO.setAccounts(customerEntity.getAccounts().stream()
                    .map(accountMapper::toAccountDTO)
                    .collect(Collectors.toList()));
        }

        return customerDTO;
    }

    public CustomerEntity toCustomerEntity(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName(customerDTO.getFirstName());
        customerEntity.setLastName(customerDTO.getLastName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhone(customerDTO.getPhone());
        customerEntity.setDateOfBirth(customerDTO.getDateOfBirth());
        return customerEntity;
    }
}
