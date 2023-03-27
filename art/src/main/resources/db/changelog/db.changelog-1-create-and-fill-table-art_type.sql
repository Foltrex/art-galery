--liquibase formatted sql
--changeset Maksim Semianko:create new table art_type in fill it

CREATE TABLE IF NOT EXISTS art_type
(
    id       INT PRIMARY KEY,
    label    VARCHAR(50),
    verified BOOLEAN
);

INSERT INTO art_type (id, label, verified)
VALUES (1, 'Picture', true);

INSERT INTO art_type (id, label, verified)
VALUES (2, 'Photo', true);

INSERT INTO art_type (id, label, verified)
VALUES (3, 'Drawing', true);

INSERT INTO art_type (id, label, verified)
VALUES (4, 'Sculpture', true);
