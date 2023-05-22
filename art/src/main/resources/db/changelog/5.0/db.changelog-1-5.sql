--liquibase formatted sql

--changeset Foltrex:1
-------------------Sequence ddl----------------------
CREATE SEQUENCE IF NOT EXISTS role_id_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1;

-------------------Extension ddl---------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-------------------Tables ddl------------------------
CREATE TABLE IF NOT EXISTS account (
    id UUID PRIMARY KEY  DEFAULT uuid_generate_v4 (),
    account_type VARCHAR(255),
    blocked_since TIMESTAMP WITHOUT TIME ZONE,
    email VARCHAR(255) UNIQUE,
    fail_count INTEGER,
    last_fail TIMESTAMP WITHOUT TIME ZONE,
    password VARCHAR(255),
    is_one_time_password BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS role (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('role_id_seq'),
    name INTEGER NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS accounts_m2m_roles (
    account_id UUID NOT NULL REFERENCES account (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    role_id BIGINT NOT NULL REFERENCES role (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    PRIMARY KEY (account_id, role_id)
);

CREATE TABLE IF NOT EXISTS email_message_code (
    id UUID PRIMARY KEY  DEFAULT uuid_generate_v4 (),
    account_id UUID NOT NULL REFERENCES account (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    code INTEGER,
    is_valid boolean,
    count_attempts INTEGER,
    created_at TIMESTAMP
);
