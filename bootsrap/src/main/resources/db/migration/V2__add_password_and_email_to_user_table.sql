ALTER TABLE `user`
    ADD COLUMN `password` varchar(100) NOT NULL;

ALTER TABLE `user`
    ADD COLUMN `email` varchar(50) NOT NULL;
