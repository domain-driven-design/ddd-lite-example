CREATE TABLE user
(
    `id`   varchar(36) not null primary key,
    `name` varchar(36) not null,
    `email` varchar(50) not null,
    `password` varchar(100) not null,
    `created_at` timestamp(3) not null default current_timestamp(3),
    `updated_at` timestamp(3) not null default current_timestamp(3) on update current_timestamp(3)
);
