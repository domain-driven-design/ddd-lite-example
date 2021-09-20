CREATE TABLE `group_request`
(
    `id`          VARCHAR(36)  NOT NULL PRIMARY KEY,
    `name`        VARCHAR(50)  NOT NULL,
    `description` TEXT         NOT NULL,
    `created_by`  VARCHAR(36)  NOT NULL,
    `created_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
);
