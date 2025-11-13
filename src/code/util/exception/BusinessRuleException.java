package code.util.exception;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessRuleException extends InternshipSystemException {

    /**
     * Constructs a new BusinessRuleException with the specified detail message.
     *
     * @param message the detail message
     */
    public BusinessRuleException(String message) {
        super(message);
    }

    /**
     * Constructs a new BusinessRuleException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
