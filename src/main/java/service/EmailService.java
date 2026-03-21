package service;

import model.Reservation;
import util.AppLogger;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for handling real email notifications using Jakarta Mail.
 * Integrates with SMTP server provided in util/db.properties.
 * 
 * Why: Moves from simulation to production-ready email delivery.
 * 
 * @author Ocean View Resort Dev Team
 */
public class EmailService {

    private static final Logger LOGGER = AppLogger.getLogger(EmailService.class);
    private final Properties props = new Properties();

    public EmailService() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("util/db.properties")) {
            if (input != null) {
                props.load(input);
                LOGGER.info("Email service initialized with config from db.properties");
            } else {
                LOGGER.severe("Unable to find util/db.properties for email configuration");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load email configurations", e);
        }
    }

    /**
     * Sends a welcome email to the guest asynchronously.
     * Starts a new thread to avoid blocking the reservation confirmation flow.
     *
     * @param reservation The reservation details
     */
    public void sendWelcomeEmail(Reservation reservation) {
        String recipient = reservation.getGuestEmail();
        if (recipient == null || recipient.trim().isEmpty()) {
            LOGGER.warning("No email address provided for guest: " + reservation.getGuestName());
            return;
        }

        // Run in a separate thread to ensure high availability of the booking process
        new Thread(() -> {
            try {
                sendEmailInternal(recipient, 
                    "Welcome to Ocean View Resort - Reservation #" + reservation.getReservationNumber(),
                    buildWelcomeBody(reservation));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to send welcome email to " + recipient, e);
            }
        }).start();
    }

    private void sendEmailInternal(String to, String subject, String body) throws MessagingException {
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(props.getProperty("mail.user")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
        LOGGER.info("Real email sent successfully to: " + to);
    }

    private String buildWelcomeBody(Reservation reservation) {
        return String.format(
            "Dear %s,\n\n" +
            "Thank you for choosing Ocean View Resort! Your reservation has been confirmed.\n\n" +
            "Reservation Details:\n" +
            "- Reservation Number: %s\n" +
            "- Room: %s (%s)\n" +
            "- Check-in: %s\n" +
            "- Check-out: %s\n" +
            "- Total Amount: $%s\n\n" +
            "We look forward to seeing you soon!\n\n" +
            "Best regards,\n" +
            "Ocean View Resort Management",
            reservation.getGuestName(),
            reservation.getReservationNumber(),
            reservation.getRoomNumber() != null ? reservation.getRoomNumber() : "N/A",
            reservation.getRoomType() != null ? reservation.getRoomType() : "N/A",
            reservation.getCheckIn(),
            reservation.getCheckOut(),
            reservation.getTotalAmount()
        );
    }
}
