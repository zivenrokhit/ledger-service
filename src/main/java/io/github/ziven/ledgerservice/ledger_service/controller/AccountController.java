package io.github.ziven.ledgerservice.ledger_service.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.ziven.ledgerservice.ledger_service.dto.request.AccountCreateRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.AccountCreateResponseDTO;
import io.github.ziven.ledgerservice.ledger_service.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public AccountCreateResponseDTO createAccount(@RequestBody AccountCreateRequestDTO request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public AccountCreateResponseDTO getAccountById(@PathVariable UUID id) {
        return accountService.getAccountById(id);
    }

}
