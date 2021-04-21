CREATE TABLE `article_tag`
(
    `id`         varchar(36)  not null primary key,
    `tag_id`     varchar(36)  not null,
    `article_id` varchar(36)  not null,
    `created_by` varchar(36)  not null,
    `created_at` timestamp(3) not null default current_timestamp(3),
    `updated_at` timestamp(3) not null default current_timestamp(3) on update current_timestamp(3)
);
