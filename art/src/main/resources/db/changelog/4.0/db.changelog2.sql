--liquibase formatted
--changeset rs:2

alter table file_info drop column filename;
alter table file_info drop column system_file_name;
alter table file_info add column content_length integer;