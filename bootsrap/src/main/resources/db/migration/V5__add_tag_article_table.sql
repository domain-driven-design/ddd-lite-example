CREATE TABLE `tag_article`
(
    `id`               varchar(50) not null primary key,
    `tag_id`           varchar(50) not null,
    `article_id`       varchar(50) not null,
    `created_by`       varchar(50) not null,
    `last_modified_at` timestamp   not null
);
