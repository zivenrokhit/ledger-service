package io.github.ziven.ledgerservice.ledger_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.ziven.ledgerservice.ledger_service.dto.request.JournalEntryResponseDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionDepositRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionResponseDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionTransferRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.dto.request.TransactionWithdrawalRequestDTO;
import io.github.ziven.ledgerservice.ledger_service.model.Account;
import io.github.ziven.ledgerservice.ledger_service.model.JournalEntry;
import io.github.ziven.ledgerservice.ledger_service.model.Transaction;
import io.github.ziven.ledgerservice.ledger_service.repository.AccountRepository;
import io.github.ziven.ledgerservice.ledger_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final String TYPE_DEPOSIT = "DEPOSIT";
    private static final String TYPE_WITHDRAWAL = "WITHDRAWAL";
    private static final String TYPE_TRANSFER = "TRANSFER";

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final JournalEntryService journalEntryService;

    @Transactional
    public TransactionResponseDTO deposit(TransactionDepositRequestDTO request) {
        BigDecimal amount = requirePositiveAmount(request.getAmount());
        String referenceId = requireReferenceId(request.getReferenceId());
        Account account = getAccount(request.getAccountId());

        Transaction transaction = createTransaction(referenceId, request.getDescription(), TYPE_DEPOSIT);
        account.setBalance(currentBalance(account).add(amount));
        accountRepository.save(account);

        JournalEntry entry = journalEntryService.createEntry(transaction, account, amount);
        JournalEntry savedEntry = journalEntryService.saveEntry(entry);

        return toResponse(transaction, List.of(savedEntry));
    }

    @Transactional
    public TransactionResponseDTO withdraw(TransactionWithdrawalRequestDTO request) {
        BigDecimal amount = requirePositiveAmount(request.getAmount());
        String referenceId = requireReferenceId(request.getReferenceId());
        Account account = getAccount(request.getAccountId());

        ensureSufficientFunds(account, amount);

        Transaction transaction = createTransaction(referenceId, request.getDescription(), TYPE_WITHDRAWAL);
        account.setBalance(currentBalance(account).subtract(amount));
        accountRepository.save(account);

        JournalEntry entry = journalEntryService.createEntry(transaction, account, amount.negate());
        JournalEntry savedEntry = journalEntryService.saveEntry(entry);

        return toResponse(transaction, List.of(savedEntry));
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferRequestDTO request) {
        BigDecimal amount = requirePositiveAmount(request.getAmount());
        String referenceId = requireReferenceId(request.getReferenceId());
        Account fromAccount = getAccount(request.getFromAccountId());
        Account toAccount = getAccount(request.getToAccountId());

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source and destination accounts must differ");
        }

        ensureSameCurrency(fromAccount, toAccount);
        ensureSufficientFunds(fromAccount, amount);

        Transaction transaction = createTransaction(referenceId, request.getDescription(), TYPE_TRANSFER);

        fromAccount.setBalance(currentBalance(fromAccount).subtract(amount));
        toAccount.setBalance(currentBalance(toAccount).add(amount));
        accountRepository.saveAll(List.of(fromAccount, toAccount));

        JournalEntry debitEntry = journalEntryService.createEntry(transaction, fromAccount, amount.negate());
        JournalEntry creditEntry = journalEntryService.createEntry(transaction, toAccount, amount);
        List<JournalEntry> savedEntries = journalEntryService.saveEntries(List.of(debitEntry, creditEntry));

        return toResponse(transaction, savedEntries);
    }

    private Account getAccount(UUID accountId) {
        if (accountId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id is required");
        }

        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    private BigDecimal requirePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        return amount;
    }

    private String requireReferenceId(String referenceId) {
        if (referenceId == null || referenceId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reference id is required");
        }

        return referenceId.trim();
    }

    private void ensureSameCurrency(Account fromAccount, Account toAccount) {
        String fromCurrency = fromAccount.getCurrency();
        String toCurrency = toAccount.getCurrency();

        if (fromCurrency == null || toCurrency == null || !fromCurrency.equals(toCurrency)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accounts must share the same currency");
        }
    }

    private void ensureSufficientFunds(Account account, BigDecimal amount) {
        BigDecimal balance = currentBalance(account);

        if (balance.compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }
    }

    private BigDecimal currentBalance(Account account) {
        return account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
    }

    private Transaction createTransaction(String referenceId, String description, String type) {
        Transaction transaction = new Transaction();
        transaction.setReferenceId(referenceId);
        transaction.setDescription(description);
        transaction.setType(type);
        return transactionRepository.save(transaction);
    }

    private TransactionResponseDTO toResponse(Transaction transaction, List<JournalEntry> entries) {
        List<JournalEntryResponseDTO> entryResponses = entries.stream()
                .map(this::toEntryResponse)
                .toList();

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceId(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getCreatedAt(),
                entryResponses);
    }

    private JournalEntryResponseDTO toEntryResponse(JournalEntry entry) {
        return new JournalEntryResponseDTO(
                entry.getId(),
                entry.getAccount().getId(),
                entry.getAmount(),
                entry.getCreatedAt());
    }
}
