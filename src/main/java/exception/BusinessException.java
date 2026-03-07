package exception;

/**
 * Custom checked exception for service-layer business rule violations.
 * Thrown when user input or business logic validation fails
 * (e.g., invalid dates, unavailable rooms, duplicate usernames).
 * 
 * Why: Separates business logic errors from infrastructure errors,
 * enabling the controller layer to provide user-friendly feedback.
 * 
 * @author Ocean View Resort Dev Team
 */
public class BusinessException extends Exception {

    /**
     * Constructs a BusinessException with a descriptive message.
     *
     * @param message Human-readable description of the violation
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructs a BusinessException wrapping a root cause.
     *
     * @param message Human-readable description
     * @param cause   The underlying exception
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
