package io.github.ziven.ledgerservice.ledger_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.ziven.ledgerservice.ledger_service.model.Account;
import io.github.ziven.ledgerservice.ledger_service.model.JournalEntry;
import io.github.ziven.ledgerservice.ledger_service.model.Transaction;
import io.github.ziven.ledgerservice.ledger_service.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(Transaction transaction, Account account, BigDecimal amount) {
        JournalEntry entry = new JournalEntry();
        entry.setTransaction(transaction);
        entry.setAccount(account);
        entry.setAmount(amount);
        return entry;
    }

    public JournalEntry saveEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }

    public List<JournalEntry> saveEntries(List<JournalEntry> entries) {
        return journalEntryRepository.saveAll(entries);
    }
}
