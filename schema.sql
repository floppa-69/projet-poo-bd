CREATE DATABASE IF NOT EXISTS pharmacy_db;
USE pharmacy_db;

-- Create Application User to avoid root access issues
CREATE USER IF NOT EXISTS 'pharmacy_user'@'localhost' IDENTIFIED BY 'pharmacy_pass';
GRANT ALL PRIVILEGES ON pharmacy_db.* TO 'pharmacy_user'@'localhost';
FLUSH PRIVILEGES;

-- Drop existing tables to ensure clean slate
DROP TABLE IF EXISTS sale_items;
DROP TABLE IF EXISTS supplier_order_items;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS supplier_orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS users;

-- Users Table (Admin, Employee)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Hashed password
    role ENUM('ADMIN', 'EMPLOYEE') NOT NULL
);

-- Suppliers Table
CREATE TABLE suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(100),
    email VARCHAR(100),
    address VARCHAR(255)
);

-- Clients Table
CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    loyalty_points INT DEFAULT 0
);

-- Products Table
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    min_stock_level INT DEFAULT 5,
    supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);

-- Sales Table
CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    client_id INT,
    user_id INT, -- Employee who made the sale
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Sale Items (Many-to-Many resolution for Sales <-> Products)
CREATE TABLE sale_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL, -- Price at the moment of sale
    FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Supplier Orders
CREATE TABLE supplier_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT NOT NULL,
    status ENUM('PENDING', 'RECEIVED', 'CANCELLED') DEFAULT 'PENDING',
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE
);

-- Order Items
CREATE TABLE supplier_order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES supplier_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Sample Data
INSERT INTO users (username, password, role) VALUES 
('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN'), -- password: admin
('user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', 'EMPLOYEE'); -- password: user

INSERT INTO suppliers (name, contact, email, address) VALUES 
('PharmaCorp', 'Alice Smith', 'alice@pharmacorp.com', '123 Med Way'),
('MediSupply', 'Bob Jones', 'bob@medisupply.com', '456 Health St');

INSERT INTO products (name, description, price, stock_quantity, min_stock_level, supplier_id) VALUES 
('Paracetamol', 'Pain reliever', 5.00, 100, 20, 1),
('Amoxicillin', 'Antibiotic', 12.50, 50, 10, 1),
('Vitamin C', 'Immune support', 8.00, 200, 30, 2);

INSERT INTO clients (name, phone, email) VALUES 
('John Doe', '555-1234', 'john@example.com');
