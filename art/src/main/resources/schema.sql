-------------------Sequence ddl----------------------
CREATE SEQUENCE IF NOT EXISTS organization_role_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    CACHE 1
    OWNED BY organization_role.id;

-------------------Tables ddl------------------------
CREATE TABLE IF NOT EXISTS city (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    city_id UUID REFERENCES city(id),
    street_name VARCHAR NOT NULL,
    street_number INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS artist (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    account_id VARCHAR(255),
    description VARCHAR(1024),
    city_id UUID REFERENCES city(id),
);

CREATE TABLE IF NOT EXISTS facility (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    is_active BOOLEAN DEFAULT true,
    name VARCHAR(255),
    address_id UUID REFERENCES address(id),
    organization_id UUID REFERENCES organization(id)
);

CREATE TABLE IF NOT EXISTS art (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    description VARCHAR(255),
    name VARCHAR(255),
    artist_id uuid REFERENCES artist(id)
);

CREATE TABLE IF NOT EXISTS art_info (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    commission DOUBLE PRECISION,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    exposition_date_end TIMESTAMP WITHOUT TIME ZONE,
    exposition_date_start TIMESTAMP WITHOUT TIME ZONE,
    price NUMERIC(19,2),
    status INTEGER,
    art_id UUID REFERENCES art(id),
    facility_id UUID REFERENCES facility(id),
    organisation_id UUID NOT NULL REFERENCES organization(id),
);

CREATE TABLE IF NOT EXISTS organization (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    status INTEGER,
    address_id UUID REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS organization_role (
    id BIGINT PRIMARY KEY DEFAULT nextval('organization_role_id_seq'::regclass),
    name INTEGER UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS proposal (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_confirmation boolean,
    commission double precision NOT NULL,
    currency bigint NOT NULL,
    organisation_confirmation boolean,
    price numeric(19,2),
    artist_id uuid NOT NULL REFERENCES artist(id),
    facility_id uuid REFERENCES facility(id),
    organisation_id uuid NOT NULL REFERENCES organization(id)
);

CREATE TABLE IF NOT EXISTS representative (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id uuid NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    facility_id uuid REFERENCES facility(id),
    organization_id uuid REFERENCES organization(id),
    organization_role_id bigint REFERENCES organization_role(id)
);