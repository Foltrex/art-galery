--liquibase formatted sql
--changeset Maksim Semianko:create new table art_topic in fill it

CREATE TABLE IF NOT EXISTS art_topic
(
    id       INT PRIMARY KEY,
    label    VARCHAR(50),
    verified BOOLEAN
);

INSERT INTO art_topic (id, label, verified)
VALUES (1, 'Animals', true);

INSERT INTO art_topic (id, label, verified)
VALUES (2, 'Pop-art', true);

INSERT INTO art_topic (id, label, verified)
VALUES (3, 'Story', true);

INSERT INTO art_topic (id, label, verified)
VALUES (4, 'Modern', true);

INSERT INTO art_topic (id, label, verified)
VALUES (5, 'Nude', true);

INSERT INTO art_topic (id, label, verified)
VALUES (6, 'Surrealism', true);

INSERT INTO art_topic (id, label, verified)
VALUES (7, 'Love', true);

INSERT INTO art_topic (id, label, verified)
VALUES (8, 'City', true);

INSERT INTO art_topic (id, label, verified)
VALUES (9, 'Nature', true);

INSERT INTO art_topic (id, label, verified)
VALUES (10, 'Abstraction', true);

INSERT INTO art_topic (id, label, verified)
VALUES (11, 'Gender', true);

INSERT INTO art_topic (id, label, verified)
VALUES (12, 'Fashion', true);

INSERT INTO art_topic (id, label, verified)
VALUES (13, 'For him', true);

INSERT INTO art_topic (id, label, verified)
VALUES (14, 'For her', true);

INSERT INTO art_topic (id, label, verified)
VALUES (15, 'Gift', true);

INSERT INTO art_topic (id, label, verified)
VALUES (16, 'Portrait', true);

INSERT INTO art_topic (id, label, verified)
VALUES (17, 'Landscape', true);

INSERT INTO art_topic (id, label, verified)
VALUES (18, 'Still life', true);

INSERT INTO art_topic (id, label, verified)
VALUES (19, 'Realism', true);
