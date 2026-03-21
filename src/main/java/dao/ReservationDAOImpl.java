package dao;

import model.Reservation;
import util.AppLogger;
import util.DBConnectionPool;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concrete DAO implementation for Reservation entity.
 * Create and delete operations use explicit JDBC transactions to ensure
 * atomicity between reservation records and room status updates.
 * 
 * @author Ocean View Resort Dev Team
 */
public class ReservationDAOImpl implements GenericDAO<Reservation> {

    private static final Logger LOGGER = AppLogger.getLogger(ReservationDAOImpl.class);

    @Override
    public Reservation findById(int id) {
        String sql = "SELECT r.*, rm.room_number, rm.room_type, u.username AS booked_by "
                + "FROM reservations r "
                + "JOIN rooms rm ON r.room_id = rm.id "
                + "JOIN users u ON r.user_id = u.id "
                + "WHERE r.id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractReservationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ReservationDAO.findById failed for id=" + id, e);
            throw new DataAccessException("Failed to find reservation by ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_number, rm.room_type, u.username AS booked_by "
                + "FROM reservations r "
                + "JOIN rooms rm ON r.room_id = rm.id "
                + "JOIN users u ON r.user_id = u.id "
                + "ORDER BY r.created_at DESC";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ReservationDAO.findAll failed.", e);
            throw new DataAccessException("Failed to retrieve all reservations.", e);
        }
        return reservations;
    }

    /**
     * Searches reservations by guest name and/or status.
     *
     * @param guestName Partial guest name filter (uses LIKE), or null
     * @param status    Exact status filter, or null
     * @return Filtered list of reservations
     */
    public List<Reservation> searchReservations(String guestName, String status) {
        List<Reservation> reservations = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, rm.room_number, rm.room_type, u.username AS booked_by "
                        + "FROM reservations r "
                        + "JOIN rooms rm ON r.room_id = rm.id "
                        + "JOIN users u ON r.user_id = u.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (guestName != null && !guestName.trim().isEmpty()) {
            sql.append(" AND r.guest_name LIKE ?");
            params.add("%" + guestName.trim() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND r.status = ?");
            params.add(status.toUpperCase());
        }
        sql.append(" ORDER BY r.created_at DESC");

        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ReservationDAO.searchReservations failed.", e);
            throw new DataAccessException("Failed to search reservations.", e);
        }
        return reservations;
    }

    /**
     * Creates a reservation within a JDBC transaction.
     * Steps: (1) Insert reservation record, (2) Update room status to OCCUPIED.
     * On failure, the entire transaction is rolled back.
     */
    @Override
    public boolean create(Reservation r) {
        String insertSql = "INSERT INTO reservations (reservation_number, user_id, room_id, guest_name, guest_email, check_in, check_out, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateRoomSql = "UPDATE rooms SET status = 'OCCUPIED' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Step 1: Insert reservation (triggers validate dates and room availability)
            try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, r.getReservationNumber());
                stmt.setInt(2, r.getUserId());
                stmt.setInt(3, r.getRoomId());
                stmt.setString(4, r.getGuestName());
                stmt.setString(5, r.getGuestEmail());
                stmt.setDate(6, Date.valueOf(r.getCheckIn()));
                stmt.setDate(7, Date.valueOf(r.getCheckOut()));
                stmt.setBigDecimal(8, r.getTotalAmount());
                stmt.setString(9, r.getStatus());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        r.setId(generatedKeys.getInt(1));
                    }
                }
            }

            // Step 2: Mark room as OCCUPIED
            try (PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql)) {
                roomStmt.setInt(1, r.getRoomId());
                roomStmt.executeUpdate();
            }

            conn.commit(); // Commit entire transaction
            LOGGER.info("Reservation created: " + r.getReservationNumber());
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    LOGGER.warning("Rolling back reservation transaction: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed.", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "ReservationDAO.create failed.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Connection cleanup failed.", ex);
                }
            }
        }
        return false;
    }

    @Override
    public boolean update(Reservation r) {
        String sql = "UPDATE reservations SET guest_name = ?, guest_email = ?, check_in = ?, check_out = ?, total_amount = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, r.getGuestName());
            stmt.setString(2, r.getGuestEmail());
            stmt.setDate(3, Date.valueOf(r.getCheckIn()));
            stmt.setDate(4, Date.valueOf(r.getCheckOut()));
            stmt.setBigDecimal(5, r.getTotalAmount());
            stmt.setString(6, r.getStatus());
            stmt.setInt(7, r.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                LOGGER.info("Reservation updated: id=" + r.getId());
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ReservationDAO.update failed for id=" + r.getId(), e);
            throw new DataAccessException("Failed to update reservation: " + r.getId(), e);
        }
    }

    /**
     * Deletes a reservation within a JDBC transaction.
     * Steps: (1) Delete reservation record, (2) Restore room status to AVAILABLE.
     */
    @Override
    public boolean delete(int id) {
        Reservation r = findById(id);
        if (r == null)
            return false;

        String deleteSql = "DELETE FROM reservations WHERE id = ?";
        String restoreRoomSql = "UPDATE rooms SET status = 'AVAILABLE' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement roomStmt = conn.prepareStatement(restoreRoomSql)) {
                roomStmt.setInt(1, r.getRoomId());
                roomStmt.executeUpdate();
            }

            conn.commit();
            LOGGER.info("Reservation cancelled: id=" + id + ", room freed: roomId=" + r.getRoomId());
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed.", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "ReservationDAO.delete failed for id=" + id, e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Connection cleanup failed.", ex);
                }
            }
        }
        return false;
    }

    /**
     * Extracts a Reservation instance from a ResultSet row,
     * including joined room and user data.
     */
    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        return new Reservation.ReservationBuilder()
                .id(rs.getInt("id"))
                .reservationNumber(rs.getString("reservation_number"))
                .userId(rs.getInt("user_id"))
                .roomId(rs.getInt("room_id"))
                .guestName(rs.getString("guest_name"))
                .guestEmail(rs.getString("guest_email"))
                .checkIn(rs.getDate("check_in").toLocalDate())
                .checkOut(rs.getDate("check_out").toLocalDate())
                .totalAmount(rs.getBigDecimal("total_amount"))
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at"))
                .roomNumber(rs.getString("room_number"))
                .roomType(rs.getString("room_type"))
                .bookedBy(rs.getString("booked_by"))
                .build();
    }
}
