alter table art add column price BIGINT;
alter table art add column currency_id UUID;
alter table art add constraint art_currency_unq foreign key (currency_id) references currency(id);