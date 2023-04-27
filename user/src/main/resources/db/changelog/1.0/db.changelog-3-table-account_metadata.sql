--liquibase formatted sql
--changeset Maksim Semianko:table_account

DROP TABLE if exists account_metadata;

create table if not exists account_metadata
(
    account_id uuid         not null,
    key        varchar(255) not null,
    value      varchar(255),
    primary key (account_id, key)
);


