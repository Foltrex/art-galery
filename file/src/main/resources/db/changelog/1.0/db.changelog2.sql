--liquibase formatted
--changeset msemianko:3

alter table file_info drop column art_id;
