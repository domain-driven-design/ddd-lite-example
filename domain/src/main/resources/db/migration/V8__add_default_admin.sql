ALTER TABLE `user`
    MODIFY COLUMN `email` VARCHAR(50);

INSERT INTO `user` (`id`, `name`, `password`, `role`)
VALUES (UUID(), 'admin', 'password', 'ADMIN');
