create table art_art_topic (
                               art_id uuid,
                               art_topic_id int,
                               constraint fk_art_id foreign key (art_id) references art(id),
                               constraint fk_art_topic foreign key (art_topic_id) references art_topic(id),
                               PRIMARY KEY (art_id, art_topic_id)
);
create table art_art_format (
                                 art_id uuid,
                                 art_format_id int,
                                 constraint fk_art_id foreign key (art_id) references art(id),
                                 constraint fk_art_format foreign key (art_format_id) references art_format(id),
                                 PRIMARY KEY (art_id, art_format_id)
  );
create table art_art_type (
                                 art_id uuid,
                                 art_type_id int,
                                 constraint fk_art_id foreign key (art_id) references art(id),
                                 constraint fk_art_type foreign key (art_type_id) references art_type(id),
                                 PRIMARY KEY (art_id, art_type_id)
  );