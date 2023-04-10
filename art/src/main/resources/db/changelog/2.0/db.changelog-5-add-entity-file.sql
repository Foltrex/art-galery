--liquibase formatted
--changeset msemianko:14-add-entity_file-table

create table entity_file
(
    id            uuid not null primary key,
    creation_date timestamp without time zone default current_timestamp,
    entity_id     uuid,
    is_primary    boolean,
    original_id   uuid,
    type          integer
);
