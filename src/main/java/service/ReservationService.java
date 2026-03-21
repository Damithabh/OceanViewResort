package service;

import dao.ReservationDAOImpl;
import model.Reservation;
import model.Room;
import pattern.observer.AdminNotificationObserver;
import pattern.observer.EmailNotificationObserver;
import pattern.observer.LoggingObserver;
import pattern.observer.ReservationObserver;
import pattern.observer.ReservationSubject;
import pattern.strategy.PricingStrategy;
import pattern.strategy.StandardPricing;
import util.AppLogger;
import util.DBConnectionPool;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service Layer for Reservation Management.
 * Integrates the Strategy Pattern for dynamic pricing and the
 * Observer Pattern for event-driven notifications.
 * 
 * Implements ReservationSubject to manage observer subscriptions.
 * 
 * @author Ocean View Resort Dev Team
 */
public class ReservationService implements ReservationSubject {

    private static final Logger LOGGER = AppLogger.getLogger(ReservationService.class);

    private final ReservationDAOImpl reservationDAO;
    private final RoomService roomService;

    /** Strategy Pattern: current pricing algorithm (swappable at runtime). */
    private PricingStrategy pricingStrategy;

    /** Observer Pattern: list of registered event observers. */
    private final List<ReservationObserver> observers;

    public ReservationService() {
        this.reservationDAO = new ReservationDAOImpl();
        this.roomService = new RoomService();

        // Default pricing strategy
        this.pricingStrategy = new StandardPricing();

        // Register default observers
        this.observers = new ArrayList<>();
        addObserver(new AdminNotificationObserver());
        addObserver(new LoggingObserver());
        addObserver(new EmailNotificationObserver());
    }

    // ============================
    // OBSERVER PATTERN METHODS
    // ============================

    @Override
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
        LOGGER.fine("Observer registered: " + observer.getClass().getSimpleName());
    }

    @Override
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a reservation creation event.
     *
     * @param reservation The newly created reservation
     */
    private void notifyCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }

    /**
     * Notifies all registered observers about a reservation cancellation event.
     *
     * @param reservation The cancelled reservation
     */
    private void notifyCancelled(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservation);
        }
    }

    // ============================
    // STRATEGY PATTERN METHODS
    // ============================

    /**
     * Sets the active pricing strategy at runtime.
     * Why: Allows the controller to switch pricing based on admin settings.
     *
     * @param strategy The pricing strategy to use
     */
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        LOGGER.info("Pricing strategy changed to: " + strategy.getStrategyName());
    }

    /**
     * Returns the currently active pricing strategy.
     *
     * @return The active PricingStrategy instance
     */
    public PricingStrategy getPricingStrategy() {
        return this.pricingStrategy;
    }

    // ============================
    // CORE BUSINESS METHODS
    // ============================

    /**
     * Retrieves all reservations with joined room and user data.
     *
     * @return List of all reservations
     */
    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    /**
     * Retrieves a single reservation by ID.
     *
     * @param id The reservation ID
     * @return The Reservation, or null if not found
     */
    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id);
    }

    /**
     * Searches reservations by guest name and/or status.
     *
     * @param guestName Partial name filter (or null)
     * @param status    Exact status filter (or null)
     * @return Filtered reservations
     */
    public List<Reservation> searchReservations(String guestName, String status) {
        return reservationDAO.searchReservations(guestName, status);
    }

    /**
     * Books a room with full business validation, Strategy-based pricing,
     * and Observer-based event notification.
     *
     * @param userId      The ID of the staff member creating the reservation
     * @param roomId      The ID of the room to book
     * @param guestName   The guest's full name
     * @param checkInStr  Check-in date string (yyyy-MM-dd)
     * @param checkOutStr Check-out date string (yyyy-MM-dd)
     * @return true if the booking was successful
     */
    public boolean bookRoom(int userId, int roomId, String guestName, String guestEmail, String checkInStr, String checkOutStr) {
        LocalDate checkIn = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);

        // 1. Business Validation: dates
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            LOGGER.warning("Invalid dates: check-out must be after check-in.");
            return false;
        }

        // 2. Check room availability
        Room room = roomService.getRoomById(roomId);
        if (room == null || !"AVAILABLE".equals(room.getStatus())) {
            LOGGER.warning("Room " + roomId + " is not available for booking.");
            return false;
        }

        // 3. Calculate price using Strategy Pattern
        BigDecimal totalAmount = pricingStrategy.calculatePrice(
                room.getPricePerNight(), checkIn, checkOut);
        LOGGER.info("Price calculated via " + pricingStrategy.getStrategyName()
                + " strategy: $" + totalAmount);

        // 4. Cross-check with Stored Procedure (advanced DB feature)
        BigDecimal dbCalculated = calculateBillUsingDB(roomId, checkInStr, checkOutStr);
        if (dbCalculated != null) {
            LOGGER.info("DB stored procedure calculated: $" + dbCalculated
                    + " | Strategy calculated: $" + totalAmount);
            // Use the Strategy-calculated amount (which accounts for surcharges)
        }

        // 5. Build Reservation using Builder Pattern
        Reservation reservation = new Reservation.ReservationBuilder()
                .reservationNumber("RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .userId(userId)
                .roomId(roomId)
                .guestName(guestName)
                .guestEmail(guestEmail)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .build();

        // 6. Persist via DAO (transactional)
        boolean success = reservationDAO.create(reservation);

        // 7. Notify Observers on success
        if (success) {
            notifyCreated(reservation);
        }

        return success;
    }

    /**
     * Cancels a reservation and notifies all observers.
     *
     * @param id The reservation ID to cancel
     * @return true if cancellation was successful
     */
    public boolean cancelReservation(int id) {
        // Fetch before delete so observers have full data
        Reservation reservation = reservationDAO.findById(id);
        if (reservation == null)
            return false;

        boolean success = reservationDAO.delete(id);

        if (success) {
            notifyCancelled(reservation);
        }

        return success;
    }

    /**
     * Calls MySQL Stored Procedure 'sp_calculate_bill' as an advanced DB feature.
     *
     * @param roomId   The room ID
     * @param checkIn  Check-in date string
     * @param checkOut Check-out date string
     * @return The calculated total, or null on failure
     */
    private BigDecimal calculateBillUsingDB(int roomId, String checkIn, String checkOut) {
        String sql = "{call sp_calculate_bill(?, ?, ?, ?)}";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, java.sql.Date.valueOf(checkIn));
            stmt.setDate(3, java.sql.Date.valueOf(checkOut));
            stmt.registerOutParameter(4, java.sql.Types.DECIMAL);

            stmt.execute();
            return stmt.getBigDecimal(4);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Stored procedure call failed. Using Strategy pricing.", e);
            return null;
        }
    }
}
