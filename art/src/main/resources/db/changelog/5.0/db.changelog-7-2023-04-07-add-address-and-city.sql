--liquibase formatted sql

--changeset Foltrex:2023-04-07-add-city-and-address
CREATE TABLE IF NOT EXISTS city (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    name VARCHAR NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    city_id UUID REFERENCES city (id),
    full_name VARCHAR NOT NULL
);