ALTER TABLE users
ADD COLUMN role VARCHAR(10) NOT NULL DEFAULT 'USER';

INSERT INTO users (name, email, password, role, created_at)
VALUES ('Guilherme', 'guilherme.adams@live.com',
        '$2a$10$8SNFHfUptpudxmCvX7KvrOeh0bMXWN61UzGJOpOXqewFzTu1mKvQK',
        'ADMIN', NOW());
