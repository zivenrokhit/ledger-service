package io.github.ziven.ledgerservice.ledger_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.ziven.ledgerservice.ledger_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
