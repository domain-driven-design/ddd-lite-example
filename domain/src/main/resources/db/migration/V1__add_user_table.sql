CREATE TABLE `user`
(
    `id`         VARCHAR(36)  NOT NULL PRIMARY KEY,
    `name`       VARCHAR(50)  NOT NULL,
    `email`      VARCHAR(50),
    `password`   VARCHAR(100) NOT NULL,
    `role`       VARCHAR(10)  NOT NULL,
    `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    UNIQUE INDEX `uni_email` (`email`)
);
