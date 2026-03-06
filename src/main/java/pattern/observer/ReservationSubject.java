package pattern.observer;

/**
 * Observer Pattern — Subject Interface.
 * Defines the contract for objects that manage a list of observers
 * and notify them of state changes.
 * 
 * @author Ocean View Resort Dev Team
 */
public interface ReservationSubject {

    /**
     * Registers an observer to receive event notifications.
     *
     * @param observer The observer to register
     */
    void addObserver(ReservationObserver observer);

    /**
     * Removes a previously registered observer.
     *
     * @param observer The observer to remove
     */
    void removeObserver(ReservationObserver observer);
}
