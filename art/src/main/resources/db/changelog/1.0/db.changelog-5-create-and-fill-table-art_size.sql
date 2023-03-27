--liquibase formatted sql
--changeset Maksim Semianko:create new table art_size in fill it

CREATE TABLE IF NOT EXISTS art_size
(
    id       INT PRIMARY KEY,
    label    VARCHAR(50),
    verified BOOLEAN
);

INSERT INTO art_size (id, label, verified)
VALUES (1, 'Small (less then 30x30)', true);

INSERT INTO art_size (id, label, verified)
VALUES (2, 'Medium (less then 80x80)', true);

INSERT INTO art_size (id, label, verified)
VALUES (3, 'Big(less then 150x150)', true);

INSERT INTO art_size (id, label, verified)
VALUES (4, 'Huge (bigger then 150x150)', true);
