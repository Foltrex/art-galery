--liquibase formatted sql
--changeset Maksim Semianko:create new table art_format in fill it

CREATE TABLE IF NOT EXISTS art_format
(
    id       INT PRIMARY KEY,
    label    VARCHAR(50),
    verified BOOLEAN
);

INSERT INTO art_format (id, label, verified)
VALUES (1, 'Horizontal', true);

INSERT INTO art_format (id, label, verified)
VALUES (2, 'Vertical', true);

INSERT INTO art_format (id, label, verified)
VALUES (3, 'Square', true);

INSERT INTO art_format (id, label, verified)
VALUES (4, 'Circle', true);

INSERT INTO art_format (id, label, verified)
VALUES (5, 'Ellipsis', true);

INSERT INTO art_format (id, label, verified)
VALUES (6, 'Diptych', true);

INSERT INTO art_format (id, label, verified)
VALUES (7, 'Triptych', true);

INSERT INTO art_format (id, label, verified)
VALUES (8, 'Polyptych', true);
