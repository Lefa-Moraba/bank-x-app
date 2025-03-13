package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.CustomerEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final AccountMapper accountMapper = new AccountMapper();

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

    public CustomerEntity toCustomerEntity(CustomerDTO dto) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName(dto.getFirstName());
        customerEntity.setLastName(dto.getLastName());
        customerEntity.setEmail(dto.getEmail());
        customerEntity.setPhone(dto.getPhone());
        customerEntity.setDateOfBirth(dto.getDateOfBirth());
        return customerEntity;
    }
}
