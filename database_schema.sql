-- ============================================================
-- Ocean View Resort — Database Schema
-- MySQL 8.x Compatible
-- Features: Tables, Indexes, Triggers, Stored Procedures, Views
-- // Learned from https://dev.mysql.com/doc/refman/8.0/en/tutorial.html
-- ============================================================

DROP DATABASE IF EXISTS ocean_view_resort;
CREATE DATABASE ocean_view_resort;
USE ocean_view_resort;

-- ============================================================
-- 1. Users Table
-- Stores staff and admin accounts for the resort system.
-- ============================================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL, -- SHA-256 produces 64 hex characters
    role ENUM('ADMIN', 'STAFF') NOT NULL DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. Rooms Table
-- Inventory of all rooms available for booking.
-- ============================================================
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type ENUM('STANDARD', 'DELUXE', 'SUITE') NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    description VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 3. Reservations Table
-- Core booking records linking guests to rooms.
-- ============================================================
CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_number VARCHAR(20) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    guest_name VARCHAR(100) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    status ENUM('CONFIRMED', 'CANCELLED', 'CHECKED_IN', 'CHECKED_OUT') NOT NULL DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE RESTRICT,
    INDEX idx_reservation_number (reservation_number),
    INDEX idx_check_in (check_in),
    INDEX idx_status (status)
);

-- ============================================================
-- 4. Audit Log Table (NEW — Observer Pattern Persistence)
-- Records all significant system events for admin review.
-- ============================================================
CREATE TABLE audit_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INT DEFAULT NULL,
    description VARCHAR(500) NOT NULL,
    performed_by VARCHAR(50) DEFAULT 'SYSTEM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_event_type (event_type),
    INDEX idx_created_at (created_at)
);

-- ============================================================
-- 5. Trigger: Validate Dates and Room Status BEFORE INSERT
-- Prevents invalid reservations at the database level.
-- // Learned from https://dev.mysql.com/doc/refman/8.0/en/trigger-syntax.html
-- ============================================================
DELIMITER //
CREATE TRIGGER before_reservation_insert
BEFORE INSERT ON reservations
FOR EACH ROW
BEGIN
    DECLARE current_room_status VARCHAR(20);

    -- Ensure Check-out is strictly after Check-in
    IF NEW.check_out <= NEW.check_in THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Check-out date must be after check-in date.';
    END IF;

    -- Ensure room is actually available
    SELECT status INTO current_room_status FROM rooms WHERE id = NEW.room_id;
    IF current_room_status != 'AVAILABLE' THEN
         SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Room is not currently available.';
    END IF;
END; //
DELIMITER ;

-- ============================================================
-- 6. Stored Procedure: Calculate Total Bill
-- Computes total cost based on room rate and stay duration.
-- // Learned from https://dev.mysql.com/doc/refman/8.0/en/create-procedure.html
-- ============================================================
DELIMITER //
CREATE PROCEDURE sp_calculate_bill(
    IN p_room_id INT,
    IN p_check_in DATE,
    IN p_check_out DATE,
    OUT p_total_amount DECIMAL(10,2)
)
BEGIN
    DECLARE v_days INT;
    DECLARE v_price_per_night DECIMAL(10,2);
    
    -- Calculate day count using DATEDIFF
    SET v_days = DATEDIFF(p_check_out, p_check_in);
    
    -- Safeguard: default to 1 day minimum
    IF v_days <= 0 THEN
        SET v_days = 1;
    END IF;
    
    -- Retrieve the per-night rate for the given room
    SELECT price_per_night INTO v_price_per_night FROM rooms WHERE id = p_room_id;
    
    -- Compute total
    SET p_total_amount = v_days * v_price_per_night;
END; //
DELIMITER ;

-- ============================================================
-- 7. View: Reservation Details (NEW — Joins Data for Reporting)
-- Provides a denormalized view for dashboard and ledger displays.
-- ============================================================
CREATE VIEW v_reservation_details AS
SELECT 
    r.id,
    r.reservation_number,
    r.guest_name,
    rm.room_number,
    rm.room_type,
    rm.price_per_night,
    r.check_in,
    r.check_out,
    r.total_amount,
    r.status,
    u.username AS booked_by,
    r.created_at
FROM reservations r
JOIN rooms rm ON r.room_id = rm.id
JOIN users u ON r.user_id = u.id
ORDER BY r.created_at DESC;

-- ============================================================
-- 8. Default Data — Admin User & Sample Rooms
-- admin123 SHA-256 hash: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- ============================================================
INSERT INTO users (username, password_hash, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

INSERT INTO rooms (room_number, room_type, price_per_night, description) VALUES 
('101', 'STANDARD', 100.00, 'Ground floor standard room with garden view'),
('102', 'STANDARD', 100.00, 'Ground floor standard room with pool access'),
('201', 'DELUXE', 200.00, 'Second floor deluxe room with balcony'),
('202', 'DELUXE', 200.00, 'Second floor deluxe room with ocean view'),
('301', 'SUITE', 500.00, 'Penthouse suite with panoramic ocean view');
