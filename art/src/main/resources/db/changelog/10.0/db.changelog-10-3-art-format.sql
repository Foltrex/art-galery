drop table art_art_format;
alter table art add column art_format_id int;
alter table art add constraint art_foramt_fk foreign key (art_format_id) references art_format(id);