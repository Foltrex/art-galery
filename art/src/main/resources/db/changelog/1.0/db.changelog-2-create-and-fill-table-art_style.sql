--liquibase formatted sql
--changeset Maksim Semianko:create new table art_style in fill it

CREATE TABLE IF NOT EXISTS art_style
(
    id       INT PRIMARY KEY,
    label    VARCHAR(50),
    verified BOOLEAN
);

INSERT INTO art_style (id, label, verified)
VALUES (1, 'Abstractionism', true);

INSERT INTO art_style (id, label, verified)
VALUES (2, 'Still life', true);

INSERT INTO art_style (id, label, verified)
VALUES (3, 'Landscape', true);

INSERT INTO art_style (id, label, verified)
VALUES (4, 'Portrait', true);

INSERT INTO art_style (id, label, verified)
VALUES (5, 'SEI', true);

INSERT INTO art_style (id, label, verified)
VALUES (6, 'Cubism', true);

INSERT INTO art_style (id, label, verified)
VALUES (7, 'Pop-art', true);

INSERT INTO art_style (id, label, verified)
VALUES (8, 'Actual art', true);

INSERT INTO art_style (id, label, verified)
VALUES (9, 'Geometric abstraction', true);

INSERT INTO art_style (id, label, verified)
VALUES (10, 'Realism', true);

INSERT INTO art_style (id, label, verified)
VALUES (11, 'Hyperrealism', true);

INSERT INTO art_style (id, label, verified)
VALUES (12, 'Impressionism', true);

INSERT INTO art_style (id, label, verified)
VALUES (13, 'Modernism', true);

INSERT INTO art_style (id, label, verified)
VALUES (14, 'Surrealism', true);

INSERT INTO art_style (id, label, verified)
VALUES (15, 'Figurative art', true);

INSERT INTO art_style (id, label, verified)
VALUES (16, 'Digital art', true);
