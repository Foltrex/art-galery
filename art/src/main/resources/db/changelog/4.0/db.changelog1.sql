--liquibase formatted
--changeset Foltrex:1

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS file_info
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    art_id UUID,
    filename VARCHAR(255),
    mime_type VARCHAR(255),
    system_file_name VARCHAR(255)
)