--liquibase formatted sql
--changeset Maksim Semianko:create_table_account_metadata

CREATE TABLE IF NOT EXISTS account_metadata
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id UUID        NOT NULL,
    key        VARCHAR(50) NOT NULL,
    value      VARCHAR(50) NOT NULL
);
