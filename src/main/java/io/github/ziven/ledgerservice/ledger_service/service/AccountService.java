package io.github.ziven.ledgerservice.ledger_service.service;

import org.springframework.stereotype.Service;

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

        return new AccountCreateResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getType(),
                saved.getCurrency(),
                saved.getBalance(),
                saved.getCreatedAt());
    }
}
