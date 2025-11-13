package code.util.exception;

/**
 * Exception thrown when attempting to create a duplicate entry.
 */
public class DuplicateEntryException extends InternshipSystemException {

    /**
     * Constructs a new DuplicateEntryException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateEntryException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateEntryException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a DuplicateEntryException for a specific entity type and identifier.
     *
     * @param entityType the type of entity
     * @param identifier the duplicate identifier
     * @return the exception
     */
    public static DuplicateEntryException forEntity(String entityType, String identifier) {
        return new DuplicateEntryException(entityType + " with identifier '" + identifier + "' already exists.");
    }
}
