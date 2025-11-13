package code.util.exception;

/**
 * Base exception class for the internship management system.
 * All custom exceptions should extend this class.
 */
public class InternshipSystemException extends Exception {

    /**
     * Constructs a new InternshipSystemException with the specified detail message.
     *
     * @param message the detail message
     */
    public InternshipSystemException(String message) {
        super(message);
    }

    /**
     * Constructs a new InternshipSystemException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public InternshipSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InternshipSystemException with the specified cause.
     *
     * @param cause the cause
     */
    public InternshipSystemException(Throwable cause) {
        super(cause);
    }
}
