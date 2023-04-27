CREATE TABLE IF NOT EXISTS city (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    name VARCHAR NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    city_id UUID REFERENCES city (id),
    name VARCHAR NOT NULL
);

alter table organization  ADD CONSTRAINT address_id_fk foreign key (address_id) REFERENCES address (id);
alter table facility  ADD CONSTRAINT address_id_fk foreign key (address_id) REFERENCES address (id);