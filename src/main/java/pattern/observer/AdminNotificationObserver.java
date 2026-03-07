package pattern.observer;

import model.Reservation;
import util.AppLogger;

import java.util.logging.Logger;

/**
 * Concrete Observer — Admin Notification Observer.
 * Simulates sending a notification to admin users when reservations
 * are created or cancelled. In production, this would integrate with
 * an email/SMS/push notification service.
 * 
 * @author Ocean View Resort Dev Team
 */
public class AdminNotificationObserver implements ReservationObserver {

    private static final Logger LOGGER = AppLogger.getLogger(AdminNotificationObserver.class);

    @Override
    public void onReservationCreated(Reservation reservation) {
        String message = String.format(
                "[ADMIN ALERT] New reservation created — ID: %s, Guest: %s, Room: %d, Total: $%s",
                reservation.getReservationNumber(),
                reservation.getGuestName(),
                reservation.getRoomId(),
                reservation.getTotalAmount());
        LOGGER.info(message);
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        String message = String.format(
                "[ADMIN ALERT] Reservation cancelled — ID: %s, Guest: %s, Room freed: %d",
                reservation.getReservationNumber(),
                reservation.getGuestName(),
                reservation.getRoomId());
        LOGGER.warning(message);
    }
}
