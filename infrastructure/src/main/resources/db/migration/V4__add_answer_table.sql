CREATE TABLE `answer`
(
    `id`          VARCHAR(36)  NOT NULL PRIMARY KEY,
    `question_id` VARCHAR(36)  NOT NULL,
    `content`     TEXT         NOT NULL,
    `created_by`  VARCHAR(36)  NOT NULL,
    `created_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    INDEX `i_question_id` (`question_id`),
    UNIQUE INDEX `uni_question_id_created_by` (`question_id`, `created_by`)
);