package service;

import dao.ReservationDAOImpl;
import model.Reservation;
import model.Room;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import util.DBConnectionPool;

/**
 * Service mapping for Reservations.
 * Includes Strategy Pattern evaluation and uses Stored Procedures.
 */
public class ReservationService {

    private final ReservationDAOImpl reservationDAO;
    private final RoomService roomService;

    public ReservationService() {
        this.reservationDAO = new ReservationDAOImpl();
        this.roomService = new RoomService();
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id);
    }

    /**
     * Completes business validations before passing to DAO.
     */
    public boolean bookRoom(int userId, int roomId, String guestName, String checkInStr, String checkOutStr) {

        LocalDate checkIn = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);

        // 1. Business Logic Validation: Ensure checkout is after checkin
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            System.err.println("❌ Invalid dates: Check-out must be after check-in.");
            return false;
        }

        // 2. Check room availability
        Room room = roomService.getRoomById(roomId);
        if (room == null || !"AVAILABLE".equals(room.getStatus())) {
            System.err.println("❌ Room is not available.");
            return false;
        }

        // 3. Calculate Bill using Stored Procedure (Advanced DB Feature Requirement)
        BigDecimal totalAmount = calculateBillUsingDB(roomId, checkInStr, checkOutStr);

        // Fallback to java logic if SP fails
        if (totalAmount == null) {
            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            totalAmount = room.getPricePerNight().multiply(new BigDecimal(days));
        }

        // 4. Create Reservation Using Builder Pattern
        Reservation reservation = new Reservation.ReservationBuilder()
                .reservationNumber("RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .userId(userId)
                .roomId(roomId)
                .guestName(guestName)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .build();

        // 5. Save (Transactional save handles room status update)
        return reservationDAO.create(reservation);
    }

    public boolean cancelReservation(int id) {
        return reservationDAO.delete(id); // DAO handles freeing the room transactionally
    }

    /**
     * Calls MySQL Stored Procedure `sp_calculate_bill`
     */
    private BigDecimal calculateBillUsingDB(int roomId, String checkIn, String checkOut) {
        String sql = "{call sp_calculate_bill(?, ?, ?, ?)}";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, java.sql.Date.valueOf(checkIn));
            stmt.setDate(3, java.sql.Date.valueOf(checkOut));

            // Register OUT parameter
            stmt.registerOutParameter(4, java.sql.Types.DECIMAL);

            stmt.execute();

            return stmt.getBigDecimal(4);
        } catch (SQLException e) {
            System.err.println("❌ Failed to call stored procedure. Falling back to Service logic.");
            e.printStackTrace();
            return null;
        }
    }
}
