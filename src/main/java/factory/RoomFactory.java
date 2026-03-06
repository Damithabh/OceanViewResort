package factory;

import model.Room;
import java.math.BigDecimal;

/**
 * Factory Pattern implementation for Room creation.
 * Encapsulates the logic for setting default properties based on room type.
 * 
 * Why Factory: Centralizes the room creation logic so that default prices,
 * status, and descriptions are consistently applied. Adding a new room type
 * only requires modifying this single class.
 * 
 * // Learned from https://refactoring.guru/design-patterns/factory-method
 * 
 * @author Ocean View Resort Dev Team
 */
public class RoomFactory {

    /** Private constructor — utility/factory class should not be instantiated. */
    private RoomFactory() {
        throw new UnsupportedOperationException("Factory class cannot be instantiated.");
    }

    /**
     * Creates a new Room instance with type-based default pricing and description.
     *
     * @param roomNumber The room number (e.g., "101")
     * @param type       The room type (STANDARD, DELUXE, SUITE)
     * @return A fully configured Room instance
     * @throws IllegalArgumentException If the room type is unknown
     */
    public static Room createRoom(String roomNumber, String type) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(type.toUpperCase());
        room.setStatus("AVAILABLE");

        // Set baseline prices and descriptions using factory logic
        switch (type.toUpperCase()) {
            case "STANDARD":
                room.setPricePerNight(new BigDecimal("100.00"));
                room.setDescription("Comfortable standard room with essential amenities");
                break;
            case "DELUXE":
                room.setPricePerNight(new BigDecimal("200.00"));
                room.setDescription("Spacious deluxe room with premium furnishings");
                break;
            case "SUITE":
                room.setPricePerNight(new BigDecimal("500.00"));
                room.setDescription("Luxury suite with panoramic views and private lounge");
                break;
            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }

        return room;
    }
}
