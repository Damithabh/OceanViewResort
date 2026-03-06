package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

/**
 * Reservation model with Builder Pattern for complex object construction.
 * Maps to the 'reservations' database table.
 * 
 * // Learned from https://refactoring.guru/design-patterns/builder
 * 
 * @author Ocean View Resort Dev Team
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

    // Additional display fields (populated from JOINs, not persisted directly)
    private String roomNumber;
    private String roomType;
    private String bookedBy;

    /**
     * Private constructor used exclusively by the Builder.
     * Why: Enforces immutability via controlled construction.
     */
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
        this.roomNumber = builder.roomNumber;
        this.roomType = builder.roomType;
        this.bookedBy = builder.bookedBy;
    }

    /** Default no-arg constructor for POJO compliance. */
    public Reservation() {
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", number='" + reservationNumber + "', guest='" + guestName + "', status='"
                + status + "'}";
    }

    // =================================================================
    // BUILDER PATTERN IMPLEMENTATION
    // Why: Reservation has many fields; Builder prevents telescoping
    // constructor anti-pattern and improves code readability.
    // =================================================================

    /**
     * Builder class for constructing Reservation instances with a fluent API.
     */
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
        private String roomNumber;
        private String roomType;
        private String bookedBy;

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

        public ReservationBuilder roomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public ReservationBuilder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public ReservationBuilder bookedBy(String bookedBy) {
            this.bookedBy = bookedBy;
            return this;
        }

        /**
         * Constructs and returns the immutable Reservation instance.
         *
         * @return A new Reservation built from the provided parameters
         */
        public Reservation build() {
            return new Reservation(this);
        }
    }
}
