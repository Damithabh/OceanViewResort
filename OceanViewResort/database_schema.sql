-- Database creation script for Ocean View Resort
-- // Learned from https://dev.mysql.com/doc/refman/8.0/en/tutorial.html

DROP DATABASE IF EXISTS ocean_view_resort;
CREATE DATABASE ocean_view_resort;
USE ocean_view_resort;

-- 1. Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL, -- SHA-256 is 64 hex characters
    role ENUM('ADMIN', 'STAFF') NOT NULL DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Rooms Table
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type ENUM('STANDARD', 'DELUXE', 'SUITE') NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Reservations Table
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
    INDEX idx_check_in (check_in)
);

-- 4. Trigger: Validate Date and Status BEFORE INSERT
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

    -- Ensure room is actually available (prevent double booking edge case via concurrent DB states)
    SELECT status INTO current_room_status FROM rooms WHERE id = NEW.room_id;
    IF current_room_status != 'AVAILABLE' THEN
         SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Room is not currently available.';
    END IF;
END; //
DELIMITER ;

-- 5. Stored Procedure: Calculate Total Bill
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
    
    -- Calculate days duration using DATEDIFF
    SET v_days = DATEDIFF(p_check_out, p_check_in);
    
    -- Default to 1 day if something goes wrong, though trigger prevents this
    IF v_days <= 0 THEN
        SET v_days = 1;
    END IF;
    
    -- Get room price
    SELECT price_per_night INTO v_price_per_night FROM rooms WHERE id = p_room_id;
    
    -- Calculate and set output total
    SET p_total_amount = v_days * v_price_per_night;
END; //
DELIMITER ;

-- Insert Default Admin User (Password: admin123 -> SHA-256 hashed)
-- admin123 hash: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
INSERT INTO users (username, password_hash, role) VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

-- Insert Sample Rooms
INSERT INTO rooms (room_number, room_type, price_per_night) VALUES 
('101', 'STANDARD', 100.00),
('102', 'STANDARD', 100.00),
('201', 'DELUXE', 200.00),
('202', 'DELUXE', 200.00),
('301', 'SUITE', 500.00);
