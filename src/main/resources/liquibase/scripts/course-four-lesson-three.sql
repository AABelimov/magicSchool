-- liquibase formatted sql

-- changeset AAbelimov:1
create index name_index on student (name);

-- changeset AAbelimov:2
create index name_and_color_index on faculty (name, color);