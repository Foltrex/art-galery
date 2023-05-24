create table support_thread (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    account_id UUID NOT NULL,
    email varchar(255),
    name varchar(255),
    subject varchar(255),
    status int,
    created_at timestamp,
    updated_at timestamp,
    CONSTRAINT fk_account_id
        FOREIGN KEY(account_id)
            REFERENCES account(id)
);
create table support (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1 (),
    account_id UUID NOT NULL,
    thread_id UUID NOT NULL,
    created_at timestamp,
    type int,
    message text,
    CONSTRAINT fk_account_id
        FOREIGN KEY(account_id)
            REFERENCES account(id),
    CONSTRAINT fk_thread
        FOREIGN KEY(thread_id)
            REFERENCES support_thread(id)
);
