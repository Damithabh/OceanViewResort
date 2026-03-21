package util;

import model.Reservation;
import service.EmailService;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Standalone utility to test real email sending.
 * 
 * Instructions:
 * 1. Fill in your real SMTP credentials in src/main/java/util/db.properties.
 * 2. Right-click this file in Eclipse → Run As → Java Application.
 * 3. Check the console for logs and your inbox for the email.
 * 
 * @author Ocean View Resort Dev Team
 */
public class EmailTest {

    public static void main(String[] args) {
        System.out.println("=== EMAIL SERVICE TEST START ===");
        
        // 1. Create a mock reservation with the target email
        Reservation testRes = new Reservation.ReservationBuilder()
                .reservationNumber("TEST-12345")
                .guestName("Test User")
                .guestEmail("lahirukawishal33@gmail.com")
                .roomNumber("101")
                .roomType("DELUXE")
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalAmount(new BigDecimal("200.00"))
                .build();

        // 2. Initialize and call EmailService
        EmailService service = new EmailService();
        service.sendWelcomeEmail(testRes);

        System.out.println("=== TEST SIGNAL SENT ===");
        System.out.println("Wait a few seconds for the background thread to finish...");
        
        try {
            Thread.sleep(5000); // Wait for async task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
