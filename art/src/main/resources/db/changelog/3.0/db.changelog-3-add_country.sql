alter table city add column country VARCHAR;
alter table city add constraint city_country unique(name, country);