CREATE TABLE `tag`
(
    `id`               varchar(50) not null primary key,
    `name`             varchar(50) not null,
    `created_by`       varchar(50) not null,
    `last_modified_at` timestamp   not null
);
