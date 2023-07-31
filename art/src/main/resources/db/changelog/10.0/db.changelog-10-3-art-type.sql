drop table art_art_type;
alter table art add column art_type_id int;
alter table art add constraint art_type_fk foreign key (art_type_id) references art_type(id);