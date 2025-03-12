package com.example.bank_x_app.controllers;

import com.example.bank_x_app.DTOs.AccountDTO;
import com.example.bank_x_app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{customer_id}")
    public List<AccountDTO> getCustomerAccounts(@PathVariable Long customer_id) {
        return accountService.getAccountsByCustomerId(customer_id);
    }
}
