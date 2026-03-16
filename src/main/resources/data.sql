-- Seed data for E-commerce Platform
-- Run this against ecommerce_db after tables exist (e.g. mysql -u root -p ecommerce_db < data.sql).
-- Admin login: admin@example.com / admin123

USE ecommerce_db;

-- 1. Roles
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- 2. Admin user (BCrypt hash for "admin123")
INSERT INTO users (email, password, full_name, enabled, created_at)
SELECT * FROM (
  SELECT
    'admin@example.com' AS email,
    '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQ6n9W6.5T8pJqFqJKZqYqYqYqYqYqYq' AS password,
    'Admin User' AS full_name,
    1 AS enabled,
    NOW() AS created_at
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');

-- 3. Link admin to both roles
INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r
WHERE u.email = 'admin@example.com' AND r.name IN ('ROLE_USER', 'ROLE_ADMIN');

-- 4. Categories
INSERT IGNORE INTO categories (name, slug, description) VALUES
  ('Electronics', 'electronics', NULL),
  ('Clothing', 'clothing', NULL);

-- 5. Products (reference categories by slug)
INSERT INTO products (name, description, price, stock, image_url, created_at, category_id)
SELECT 'Wireless Headphones', 'Noise-cancelling wireless headphones.', 79.99, 50, NULL, NOW(), (SELECT id FROM categories WHERE slug = 'electronics' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Wireless Headphones' LIMIT 1);

INSERT INTO products (name, description, price, stock, image_url, created_at, category_id)
SELECT 'USB-C Cable', 'Durable USB-C to USB-A cable, 2m.', 12.99, 200, NULL, NOW(), (SELECT id FROM categories WHERE slug = 'electronics' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'USB-C Cable' LIMIT 1);

INSERT INTO products (name, description, price, stock, image_url, created_at, category_id)
SELECT 'Cotton T-Shirt', 'Plain cotton t-shirt, multiple colors.', 19.99, 100, NULL, NOW(), (SELECT id FROM categories WHERE slug = 'clothing' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Cotton T-Shirt' LIMIT 1);
