-- ============================================================
--  SMART CANTEEN - database schema (MySQL 8)
--  Run once:   mysql -u root -p < schema.sql
--  To start again from scratch:  DROP DATABASE smart_canteen;  and run this file again.
-- ============================================================

CREATE DATABASE IF NOT EXISTS smart_canteen
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE smart_canteen;

-- Students, faculty and canteen admins all live in one table; "role" tells them apart.
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    college_id  VARCHAR(30)  NOT NULL UNIQUE,          -- STU001, ADMIN001 ...
    email       VARCHAR(120) NOT NULL,
    password    VARCHAR(200) NOT NULL,                 -- salted PBKDF2 hash, never plain text
    role        ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT'
);

CREATE TABLE IF NOT EXISTS food_items (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL UNIQUE,
    description  VARCHAR(255) NOT NULL DEFAULT '',
    category     ENUM('Breakfast', 'Snacks', 'Meals', 'Beverages') NOT NULL,
    price        DECIMAL(8,2) NOT NULL CHECK (price > 0),
    image        VARCHAR(255) NOT NULL DEFAULT '',     -- image URL (optional)
    available    BOOLEAN NOT NULL DEFAULT TRUE
);

-- Order ids start at 1001, so the first order shows as "SC1001".
CREATE TABLE IF NOT EXISTS orders (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT NOT NULL,
    total_amount  DECIMAL(10,2) NOT NULL,
    order_date    DATETIME NOT NULL,                   -- when the order was placed
    pickup_time   DATETIME NOT NULL,                   -- chosen pickup date + slot
    status        ENUM('PLACED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED')
                  NOT NULL DEFAULT 'PLACED',
    FOREIGN KEY (user_id) REFERENCES users(id)
) AUTO_INCREMENT = 1001;

-- One row per food item in an order. "price" is the unit price at the time of ordering.
CREATE TABLE IF NOT EXISTS order_items (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    order_id  INT NOT NULL,
    food_id   INT NOT NULL,
    quantity  INT NOT NULL CHECK (quantity > 0),
    price     DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id)  REFERENCES food_items(id)
);

-- Simulated (demo) payments. No real money is ever involved.
CREATE TABLE IF NOT EXISTS payments (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    order_id        INT NOT NULL UNIQUE,
    amount          DECIMAL(10,2) NOT NULL,
    payment_method  ENUM('UPI', 'CARD', 'CASH') NOT NULL,
    payment_status  ENUM('SUCCESS', 'PENDING', 'REFUNDED', 'CANCELLED') NOT NULL,
    transaction_id  VARCHAR(30) NOT NULL,
    payment_date    DATETIME NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
