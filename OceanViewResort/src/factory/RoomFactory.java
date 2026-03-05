package factory;

import model.Room;
import java.math.BigDecimal;

/**
 * Factory Pattern implementation for Room instantiation.
 * // Learned from https://refactoring.guru/design-patterns/factory-method
 */
public class RoomFactory {

    public static Room createRoom(String roomNumber, String type) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(type.toUpperCase());
        room.setStatus("AVAILABLE");

        // Set baseline prices using factory logic
        switch (type.toUpperCase()) {
            case "STANDARD":
                room.setPricePerNight(new BigDecimal("100.00"));
                break;
            case "DELUXE":
                room.setPricePerNight(new BigDecimal("200.00"));
                break;
            case "SUITE":
                room.setPricePerNight(new BigDecimal("500.00"));
                break;
            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }

        return room;
    }
}
