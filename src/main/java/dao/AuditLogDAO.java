package dao;

import util.AppLogger;
import util.DBConnectionPool;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for the audit_log table.
 * Provides read-only operations for viewing system event history.
 * 
 * Why: The audit_log table is populated by the Observer pattern
 * (LoggingObserver).
 * This DAO enables the UI to display those records for administrator review.
 * 
 * @author Ocean View Resort Dev Team
 */
public class AuditLogDAO {

    private static final Logger LOGGER = AppLogger.getLogger(AuditLogDAO.class);

    /**
     * Retrieves all audit log entries, most recent first.
     *
     * @return List of audit log entries as Maps (flexible schema)
     */
    public List<Map<String, Object>> findAll() {
        return executeQuery("SELECT * FROM audit_log ORDER BY created_at DESC", null);
    }

    /**
     * Retrieves audit log entries filtered by event type.
     *
     * @param eventType The event type filter (e.g., RESERVATION_CREATED)
     * @return Filtered list of audit entries
     */
    public List<Map<String, Object>> findByEventType(String eventType) {
        return executeQuery(
                "SELECT * FROM audit_log WHERE event_type = ? ORDER BY created_at DESC",
                new Object[] { eventType });
    }

    /**
     * Retrieves audit log entries filtered by entity type and optional event type.
     *
     * @param entityType Entity filter (e.g., RESERVATION)
     * @param eventType  Event filter or null
     * @return Filtered entries
     */
    public List<Map<String, Object>> search(String entityType, String eventType) {
        StringBuilder sql = new StringBuilder("SELECT * FROM audit_log WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (entityType != null && !entityType.isEmpty()) {
            sql.append(" AND entity_type = ?");
            params.add(entityType);
        }
        if (eventType != null && !eventType.isEmpty()) {
            sql.append(" AND event_type = ?");
            params.add(eventType);
        }
        sql.append(" ORDER BY created_at DESC");

        return executeQuery(sql.toString(), params.toArray());
    }

    /**
     * Gets monthly reservation summary data for reports.
     * Returns count and total revenue grouped by month.
     *
     * @return List of monthly summaries (month, count, total_revenue)
     */
    public List<Map<String, Object>> getMonthlyReservationSummary() {
        String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, "
                + "COUNT(*) AS booking_count, "
                + "SUM(total_amount) AS total_revenue "
                + "FROM reservations "
                + "WHERE status != 'CANCELLED' "
                + "GROUP BY DATE_FORMAT(created_at, '%Y-%m') "
                + "ORDER BY month DESC";
        return executeQuery(sql, null);
    }

    /**
     * Generic query executor returning results as a list of Maps.
     * Why Maps: Avoids creating a dedicated model class for read-only reporting
     * data.
     */
    private List<Map<String, Object>> executeQuery(String sql, Object[] params) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "AuditLogDAO query failed: " + sql, e);
            throw new DataAccessException("Failed to execute audit log query.", e);
        }
        return results;
    }
}
