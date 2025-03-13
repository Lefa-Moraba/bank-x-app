package com.example.bank_x_app.mappers;

import com.example.bank_x_app.DTOs.CustomerDTO;
import com.example.bank_x_app.entities.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toCustomerDTO(CustomerEntity entity) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(entity.getId());
        customerDTO.setFirstName(entity.getFirstName());
        customerDTO.setLastName(entity.getLastName());
        customerDTO.setEmail(entity.getEmail());
        customerDTO.setPhone(entity.getPhone());
        customerDTO.setDateOfBirth(entity.getDateOfBirth());
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
