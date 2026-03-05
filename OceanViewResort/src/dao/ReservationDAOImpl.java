package dao;

import model.Reservation;
import util.DBConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements GenericDAO<Reservation> {

    @Override
    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractReservationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations ORDER BY created_at DESC";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public boolean create(Reservation r) {
        String sql = "INSERT INTO reservations (reservation_number, user_id, room_id, guest_name, check_in, check_out, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Use Transaction to handle creation + room status update
        Connection conn = null;
        try {
            conn = DBConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Reservation (Triggers on DB side will validate dates and room
            // availability)
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, r.getReservationNumber());
                stmt.setInt(2, r.getUserId());
                stmt.setInt(3, r.getRoomId());
                stmt.setString(4, r.getGuestName());
                stmt.setDate(5, Date.valueOf(r.getCheckIn()));
                stmt.setDate(6, Date.valueOf(r.getCheckOut()));
                stmt.setBigDecimal(7, r.getTotalAmount());
                stmt.setString(8, r.getStatus());

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

            // 2. Update Room Status to OCCUPIED
            String updateRoomSql = "UPDATE rooms SET status = 'OCCUPIED' WHERE id = ?";
            try (PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql)) {
                roomStmt.setInt(1, r.getRoomId());
                roomStmt.executeUpdate();
            }

            conn.commit(); // End transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println("Rolling back transaction due to error: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean update(Reservation r) {
        String sql = "UPDATE reservations SET guest_name = ?, check_in = ?, check_out = ?, total_amount = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getGuestName());
            stmt.setDate(2, Date.valueOf(r.getCheckIn()));
            stmt.setDate(3, Date.valueOf(r.getCheckOut()));
            stmt.setBigDecimal(4, r.getTotalAmount());
            stmt.setString(5, r.getStatus());
            stmt.setInt(6, r.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Soft delete or status update preferred, but implementing hard delete for
        // GenericDAO compliance
        // We must also free the room.
        Reservation r = findById(id);
        if (r == null)
            return false;

        String sql = "DELETE FROM reservations WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            String roomSql = "UPDATE rooms SET status = 'AVAILABLE' WHERE id = ?";
            try (PreparedStatement roomStmt = conn.prepareStatement(roomSql)) {
                roomStmt.setInt(1, r.getRoomId());
                roomStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        return new Reservation.ReservationBuilder()
                .id(rs.getInt("id"))
                .reservationNumber(rs.getString("reservation_number"))
                .userId(rs.getInt("user_id"))
                .roomId(rs.getInt("room_id"))
                .guestName(rs.getString("guest_name"))
                .checkIn(rs.getDate("check_in").toLocalDate())
                .checkOut(rs.getDate("check_out").toLocalDate())
                .totalAmount(rs.getBigDecimal("total_amount"))
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
