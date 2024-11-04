ALTER TABLE users
ADD COLUMN role VARCHAR(10) NOT NULL DEFAULT 'USER';

INSERT INTO users (name, email, password, role, created_at)
VALUES ('Guilherme', 'guilherme.adams@live.com',
        '$2a$10$Vc1n1kEDjF1JIZcMq0xB5Ox8HNB29O/o8XkskPmHtQkFH9Fx/HRMS',
        'ADMIN', NOW());
