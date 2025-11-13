package code.util.exception;

/**
 * Exception thrown when an unauthorized action is attempted.
 */
public class UnauthorizedException extends InternshipSystemException {

    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an UnauthorizedException for an action requiring specific role.
     *
     * @param action the action attempted
     * @param requiredRole the required role
     * @return the exception
     */
    public static UnauthorizedException forAction(String action, String requiredRole) {
        return new UnauthorizedException("Action '" + action + "' requires " + requiredRole + " role.");
    }
}
