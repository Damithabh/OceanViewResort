package pattern.observer;

import model.Reservation;
import util.AppLogger;
import util.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concrete Observer — Logging Observer.
 * Persists audit trail entries to the 'audit_log' database table
 * whenever a reservation event occurs.
 * 
 * Why: Provides a durable, queryable record of all system events
 * for compliance, debugging, and admin dashboard displays.
 * 
 * @author Ocean View Resort Dev Team
 */
public class LoggingObserver implements ReservationObserver {

    private static final Logger LOGGER = AppLogger.getLogger(LoggingObserver.class);

    @Override
    public void onReservationCreated(Reservation reservation) {
        String description = String.format(
                "Reservation %s created for guest '%s', Room ID: %d, Amount: $%s",
                reservation.getReservationNumber(),
                reservation.getGuestName(),
                reservation.getRoomId(),
                reservation.getTotalAmount());
        persistAuditLog("RESERVATION_CREATED", "RESERVATION", reservation.getId(), description);
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        String description = String.format(
                "Reservation %s cancelled for guest '%s', Room ID: %d freed",
                reservation.getReservationNumber(),
                reservation.getGuestName(),
                reservation.getRoomId());
        persistAuditLog("RESERVATION_CANCELLED", "RESERVATION", reservation.getId(), description);
    }

    /**
     * Inserts an audit log record into the database.
     *
     * @param eventType   The category of event (e.g., RESERVATION_CREATED)
     * @param entityType  The type of entity affected (e.g., RESERVATION)
     * @param entityId    The primary key of the affected entity
     * @param description Human-readable event description
     */
    private void persistAuditLog(String eventType, String entityType, int entityId, String description) {
        String sql = "INSERT INTO audit_log (event_type, entity_type, entity_id, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventType);
            stmt.setString(2, entityType);
            stmt.setInt(3, entityId);
            stmt.setString(4, description);
            stmt.executeUpdate();

            LOGGER.fine("Audit log recorded: " + eventType);
        } catch (SQLException e) {
            // Audit logging failure should not break the main flow
            LOGGER.log(Level.WARNING, "Failed to persist audit log entry: " + eventType, e);
        }
    }
}
