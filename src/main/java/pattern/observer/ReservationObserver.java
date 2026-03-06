package pattern.observer;

import model.Reservation;

/**
 * Observer Pattern — Observer Interface for Reservation Events.
 * Concrete observers implement this to react to reservation lifecycle events.
 * 
 * Why Observer: Decouples event-producing code (reservation service)
 * from event-consuming code (notifications, logging, analytics).
 * New observers can be added without modifying the subject.
 * 
 * // Learned from https://refactoring.guru/design-patterns/observer
 * 
 * @author Ocean View Resort Dev Team
 */
public interface ReservationObserver {

    /**
     * Called when a new reservation is successfully created.
     *
     * @param reservation The newly created reservation
     */
    void onReservationCreated(Reservation reservation);

    /**
     * Called when an existing reservation is cancelled.
     *
     * @param reservation The cancelled reservation
     */
    void onReservationCancelled(Reservation reservation);
}
