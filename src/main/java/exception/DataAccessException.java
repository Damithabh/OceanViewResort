package exception;

/**
 * Custom unchecked exception for data access layer failures.
 * Wraps raw SQLExceptions into a domain-specific runtime exception
 * so that service-layer code does not need to handle JDBC internals.
 * 
 * Why unchecked: DAO failures are typically unrecoverable at the call site.
 * Wrapping them as RuntimeException allows cleaner service-layer code
 * while preserving the original cause for debugging.
 * 
 * @author Ocean View Resort Dev Team
 */
public class DataAccessException extends RuntimeException {

    /**
     * Constructs a DataAccessException with a descriptive message.
     *
     * @param message Human-readable description of the data access failure
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a DataAccessException wrapping a root cause.
     *
     * @param message Human-readable description
     * @param cause   The underlying SQLException
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
