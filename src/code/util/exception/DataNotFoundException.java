package code.util.exception;

/**
 * Exception thrown when requested data is not found.
 */
public class DataNotFoundException extends InternshipSystemException {

    /**
     * Constructs a new DataNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public DataNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a DataNotFoundException for a specific entity type and ID.
     *
     * @param entityType the type of entity
     * @param id the ID that was not found
     * @return the exception
     */
    public static DataNotFoundException forEntity(String entityType, String id) {
        return new DataNotFoundException(entityType + " with ID '" + id + "' not found.");
    }
}
