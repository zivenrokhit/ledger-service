package io.github.ziven.ledgerservice.ledger_service.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.ziven.ledgerservice.ledger_service.dto.request.AccountCreateRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.AccountCreateResponseDTO;
import io.github.ziven.ledgerservice.ledger_service.model.Account;
import io.github.ziven.ledgerservice.ledger_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountCreateResponseDTO createAccount(AccountCreateRequestDTO request) {

        Account account = new Account();
        account.setName(request.getName());
        account.setType(request.getType());
        account.setCurrency(request.getCurrency());

        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    public AccountCreateResponseDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return toResponse(account);
    }

    private AccountCreateResponseDTO toResponse(Account account) {
        return new AccountCreateResponseDTO(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedAt());
    }
}
