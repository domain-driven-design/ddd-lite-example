ALTER TABLE `question`
    ADD COLUMN `group_id` VARCHAR(36) NOT NULL;

ALTER TABLE `question`
    ADD INDEX `i_group` (`group_id`);