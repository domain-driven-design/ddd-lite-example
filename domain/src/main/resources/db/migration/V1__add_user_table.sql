CREATE TABLE user
(
    `id`         VARCHAR(36)  NOT NULL PRIMARY KEY,
    `name`       VARCHAR(50)  NOT NULL,
    `email`      VARCHAR(50),
    `password`   VARCHAR(100) NOT NULL,
    `role`       VARCHAR(10)  NOT NULL,
    `created_at` TIMESTAMP(3) NOT NULL,
    `updated_at` TIMESTAMP(3) NOT NULL
);
