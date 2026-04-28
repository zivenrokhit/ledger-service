--liquibase formatted sql

--changeset zivenrokhit:1
CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    balance NUMERIC(19, 4) DEFAULT 0.0000,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE accounts;

--changeset zivenrokhit:2
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    reference_id VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE transactions;

--changeset zivenrokhit:3
CREATE TABLE journal_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL REFERENCES transactions(id),
    account_id UUID NOT NULL REFERENCES accounts(id),
    amount NUMERIC(19, 4) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE journal_entries;
