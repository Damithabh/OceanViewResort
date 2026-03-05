package service;

import dao.RoomDAOImpl;
import model.Room;
import java.util.List;

public class RoomService {
    private final RoomDAOImpl roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAOImpl();
    }

    public List<Room> getAvailableRooms() {
        return roomDAO.findAvailableRooms();
    }

    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    public Room getRoomById(int id) {
        return roomDAO.findById(id);
    }
}
