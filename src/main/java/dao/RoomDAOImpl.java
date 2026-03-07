package dao;

import model.Room;
import util.AppLogger;
import util.DBConnectionPool;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concrete DAO implementation for Room entity.
 * Supports full CRUD, available-room filtering, and type/status search.
 * 
 * @author Ocean View Resort Dev Team
 */
public class RoomDAOImpl implements GenericDAO<Room> {

    private static final Logger LOGGER = AppLogger.getLogger(RoomDAOImpl.class);

    @Override
    public Room findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRoomFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.findById failed for id=" + id, e);
            throw new DataAccessException("Failed to find room by ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number ASC";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.findAll failed.", e);
            throw new DataAccessException("Failed to retrieve all rooms.", e);
        }
        return rooms;
    }

    /**
     * Retrieves only rooms with status 'AVAILABLE'.
     *
     * @return List of available rooms
     */
    public List<Room> findAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'AVAILABLE' ORDER BY room_number ASC";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.findAvailableRooms failed.", e);
            throw new DataAccessException("Failed to retrieve available rooms.", e);
        }
        return rooms;
    }

    /**
     * Searches rooms by type and/or status.
     * Pass null for either parameter to skip that filter.
     *
     * @param type   Room type filter (STANDARD, DELUXE, SUITE) or null
     * @param status Room status filter (AVAILABLE, OCCUPIED, MAINTENANCE) or null
     * @return Filtered list of rooms
     */
    public List<Room> searchRooms(String type, String status) {
        List<Room> rooms = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM rooms WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND room_type = ?");
            params.add(type.toUpperCase());
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status.toUpperCase());
        }
        sql.append(" ORDER BY room_number ASC");

        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(extractRoomFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.searchRooms failed.", e);
            throw new DataAccessException("Failed to search rooms.", e);
        }
        return rooms;
    }

    @Override
    public boolean create(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, status, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setString(4, room.getStatus());
            stmt.setString(5, room.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        room.setId(generatedKeys.getInt(1));
                    }
                }
                LOGGER.info("Room created: " + room.getRoomNumber());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.create failed for room=" + room.getRoomNumber(), e);
            throw new DataAccessException("Failed to create room: " + room.getRoomNumber(), e);
        }
        return false;
    }

    @Override
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price_per_night = ?, status = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setString(4, room.getStatus());
            stmt.setString(5, room.getDescription());
            stmt.setInt(6, room.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                LOGGER.info("Room updated: id=" + room.getId());
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.update failed for id=" + room.getId(), e);
            throw new DataAccessException("Failed to update room: " + room.getId(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                LOGGER.info("Room deleted: id=" + id);
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "RoomDAO.delete failed for id=" + id, e);
            throw new DataAccessException("Failed to delete room: " + id, e);
        }
    }

    /**
     * Extracts a Room instance from a ResultSet row.
     */
    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room(
                rs.getInt("id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getBigDecimal("price_per_night"),
                rs.getString("status"),
                rs.getString("description"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        return room;
    }
}
