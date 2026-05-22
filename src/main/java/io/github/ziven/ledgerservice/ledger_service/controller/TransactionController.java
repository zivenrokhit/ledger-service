package io.github.ziven.ledgerservice.ledger_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionDepositRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionResponseDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionTransferRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionWithdrawalRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.service.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public TransactionResponseDTO deposit(@RequestBody TransactionDepositRequestDTO request) {
        return transactionService.deposit(request);
    }

    @PostMapping("/withdrawal")
    public TransactionResponseDTO withdraw(@RequestBody TransactionWithdrawalRequestDTO request) {
        return transactionService.withdraw(request);
    }

    @PostMapping("/transfer")
    public TransactionResponseDTO transfer(@RequestBody TransactionTransferRequestDTO request) {
        return transactionService.transfer(request);
    }
}
