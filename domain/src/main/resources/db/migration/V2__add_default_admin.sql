INSERT INTO `user` (`id`, `name`, `password`, `role`, `created_at`, `updated_at`)
VALUES (UUID(), 'admin', '$2a$10$SLzK3dPy3z4XgAOdEj/vlOvZ0HlE96sGnM/u19MJkYv8r.fB2Omnq', 'ADMIN', now(), now());
