package pattern.observer;

import model.Reservation;
import service.EmailService;
import util.AppLogger;

import java.util.logging.Logger;

/**
 * Concrete Observer for sending email notifications.
 * Why Observer: Allows adding email functionality without tightly coupling
 * the reservation service to the email logic.
 * 
 * @author Ocean View Resort Dev Team
 */
public class EmailNotificationObserver implements ReservationObserver {

    private static final Logger LOGGER = AppLogger.getLogger(EmailNotificationObserver.class);
    private final EmailService emailService;

    public EmailNotificationObserver() {
        this.emailService = new EmailService();
    }

    @Override
    public void onReservationCreated(Reservation reservation) {
        LOGGER.info("Booking event detected for: " + reservation.getReservationNumber());
        emailService.sendWelcomeEmail(reservation);
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        // Option: Send cancellation email here if needed in the future.
        LOGGER.info("Cancellation event detected for: " + reservation.getReservationNumber());
    }
}
