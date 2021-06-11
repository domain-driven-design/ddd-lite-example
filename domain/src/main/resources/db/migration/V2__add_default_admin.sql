INSERT INTO `user` (`id`, `name`, `password`, `role`, `created_at`, `updated_at`)
VALUES (UUID(), 'admin', 'password', 'ADMIN', now(), now());
