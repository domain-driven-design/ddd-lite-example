CREATE TABLE `article`
(
    `id`               varchar(50)  not null primary key,
    `title`            varchar(100) not null,
    `content`          text         not null,
    `created_by`       varchar(100) not null,
    `last_modified_at` timestamp    not null
);
