--liquibase formatted sql
--changeset Maksim Semianko:alter_id_table_account

DROP TABLE account_metadata;

CREATE TABLE IF NOT EXISTS account_metadata
(
    id         INTEGER PRIMARY KEY,
    account_id UUID        NOT NULL,
    key        VARCHAR(50) NOT NULL,
    value      VARCHAR(50) NOT NULL
);


