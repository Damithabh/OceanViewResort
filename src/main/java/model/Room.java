package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Room model representing a bookable room in the resort.
 * Maps directly to the 'rooms' database table.
 * 
 * @author Ocean View Resort Dev Team
 */
public class Room {
    private int id;
    private String roomNumber;
    private String roomType; // STANDARD, DELUXE, SUITE
    private BigDecimal pricePerNight;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE
    private String description; // Optional room description for admin panel
    private Timestamp createdAt;

    /** Default no-arg constructor for POJO compliance. */
    public Room() {
    }

    /**
     * Constructor for creating a Room from database results.
     *
     * @param id            The unique room identifier
     * @param roomNumber    The room number (e.g., "101")
     * @param roomType      The room type (STANDARD, DELUXE, SUITE)
     * @param pricePerNight Nightly rate
     * @param status        Current room availability status
     * @param description   Human-readable room description
     */
    public Room(int id, String roomNumber, String roomType, BigDecimal pricePerNight, String status,
            String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.description = description;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Room{id=" + id + ", number='" + roomNumber + "', type='" + roomType + "', status='" + status + "'}";
    }
}
