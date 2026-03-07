package service;

import dao.RoomDAOImpl;
import factory.RoomFactory;
import model.Room;
import util.AppLogger;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service Layer for Room Management.
 * Provides CRUD operations and search/filter capabilities.
 * Uses RoomFactory for standardized room creation.
 * 
 * @author Ocean View Resort Dev Team
 */
public class RoomService {

    private static final Logger LOGGER = AppLogger.getLogger(RoomService.class);
    private final RoomDAOImpl roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAOImpl();
    }

    /**
     * Retrieves all rooms in the system.
     *
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    /**
     * Retrieves only rooms with AVAILABLE status.
     *
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms() {
        return roomDAO.findAvailableRooms();
    }

    /**
     * Finds a room by its primary key.
     *
     * @param id The room ID
     * @return The Room, or null if not found
     */
    public Room getRoomById(int id) {
        return roomDAO.findById(id);
    }

    /**
     * Searches rooms by type and/or status.
     *
     * @param type   Room type filter (or null)
     * @param status Room status filter (or null)
     * @return Filtered list of rooms
     */
    public List<Room> searchRooms(String type, String status) {
        return roomDAO.searchRooms(type, status);
    }

    /**
     * Creates a new room using the RoomFactory for default property assignment.
     *
     * @param roomNumber  The room number (e.g., "401")
     * @param type        The room type (STANDARD, DELUXE, SUITE)
     * @param customPrice Optional custom price override (null to use factory
     *                    default)
     * @param description Optional description (null to use factory default)
     * @return true if the room was created successfully
     */
    public boolean createRoom(String roomNumber, String type, BigDecimal customPrice, String description) {
        Room room = RoomFactory.createRoom(roomNumber, type);

        // Override factory defaults if custom values are provided
        if (customPrice != null && customPrice.compareTo(BigDecimal.ZERO) > 0) {
            room.setPricePerNight(customPrice);
        }
        if (description != null && !description.trim().isEmpty()) {
            room.setDescription(description.trim());
        }

        boolean created = roomDAO.create(room);
        if (created) {
            LOGGER.info("Room created via Factory: " + roomNumber + " (" + type + ")");
        }
        return created;
    }

    /**
     * Updates an existing room's properties.
     *
     * @param room The room with updated fields
     * @return true if the update succeeded
     */
    public boolean updateRoom(Room room) {
        return roomDAO.update(room);
    }

    /**
     * Deletes a room by its primary key.
     *
     * @param id The room ID
     * @return true if the deletion succeeded
     */
    public boolean deleteRoom(int id) {
        return roomDAO.delete(id);
    }
}
