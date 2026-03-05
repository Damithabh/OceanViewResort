package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

/**
 * Reservation Model
 * Implementing Builder Pattern for complex object creation.
 * // Learned from https://refactoring.guru/design-patterns/builder
 */
public class Reservation {
    private int id;
    private String reservationNumber;
    private int userId;
    private int roomId;
    private String guestName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp createdAt;

    // Private constructor used by Builder
    private Reservation(ReservationBuilder builder) {
        this.id = builder.id;
        this.reservationNumber = builder.reservationNumber;
        this.userId = builder.userId;
        this.roomId = builder.roomId;
        this.guestName = builder.guestName;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.totalAmount = builder.totalAmount;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }

    // Default constructor for standard persistence frameworks/POJO adherence
    public Reservation() {
    }

    // Getters and Setters ...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ... remaining standard setters/getters
    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // --- BUILDER IMPLEMENTATION ---
    public static class ReservationBuilder {
        private int id;
        private String reservationNumber;
        private int userId;
        private int roomId;
        private String guestName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private BigDecimal totalAmount;
        private String status;
        private Timestamp createdAt;

        public ReservationBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder reservationNumber(String num) {
            this.reservationNumber = num;
            return this;
        }

        public ReservationBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public ReservationBuilder roomId(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public ReservationBuilder guestName(String name) {
            this.guestName = name;
            return this;
        }

        public ReservationBuilder checkIn(LocalDate date) {
            this.checkIn = date;
            return this;
        }

        public ReservationBuilder checkOut(LocalDate date) {
            this.checkOut = date;
            return this;
        }

        public ReservationBuilder totalAmount(BigDecimal amount) {
            this.totalAmount = amount;
            return this;
        }

        public ReservationBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ReservationBuilder createdAt(Timestamp ts) {
            this.createdAt = ts;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}
