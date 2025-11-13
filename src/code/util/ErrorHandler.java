package code.util;

import code.util.exception.InternshipSystemException;

/**
 * Centralized error handler for the internship management system.
 * Provides consistent error handling and user-friendly error messages.
 */
public class ErrorHandler {

    private ErrorHandler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Handles an exception by logging it and displaying a user-friendly message.
     *
     * @param e the exception to handle
     */
    public static void handleException(Exception e) {
        Logger.error("Exception occurred: " + e.getMessage(), e);
        
        if (e instanceof InternshipSystemException) {
            handleSystemException((InternshipSystemException) e);
        } else {
            handleGenericException(e);
        }
    }

    /**
     * Handles a system exception.
     *
     * @param e the system exception
     */
    private static void handleSystemException(InternshipSystemException e) {
        ConsoleUtil.printError(e.getMessage());
        
        String suggestion = getSuggestion(e);
        if (StringUtil.isNotEmpty(suggestion)) {
            ConsoleUtil.printInfo("Suggestion: " + suggestion);
        }
    }

    /**
     * Handles a generic exception.
     *
     * @param e the exception
     */
    private static void handleGenericException(Exception e) {
        ConsoleUtil.printError("An unexpected error occurred: " + e.getMessage());
        ConsoleUtil.printInfo("Please try again or contact support if the problem persists.");
    }

    /**
     * Gets a suggestion for recovering from an exception.
     *
     * @param e the exception
     * @return a suggestion message, or null if none available
     */
    private static String getSuggestion(InternshipSystemException e) {
        String className = e.getClass().getSimpleName();
        
        switch (className) {
            case "ValidationException":
                return "Please check your input and ensure all required fields are filled correctly.";
            
            case "DataNotFoundException":
                return "Please verify the ID or search criteria and try again.";
            
            case "DuplicateEntryException":
                return "Please use a different identifier or update the existing entry.";
            
            case "UnauthorizedException":
                return "Please ensure you have the necessary permissions or contact an administrator.";
            
            case "BusinessRuleException":
                return "Please review the business rules and adjust your request accordingly.";
            
            default:
                return null;
        }
    }

    /**
     * Handles an exception with a custom message.
     *
     * @param e the exception
     * @param customMessage the custom message to display
     */
    public static void handleException(Exception e, String customMessage) {
        Logger.error(customMessage + ": " + e.getMessage(), e);
        ConsoleUtil.printError(customMessage);
        
        if (e instanceof InternshipSystemException) {
            String suggestion = getSuggestion((InternshipSystemException) e);
            if (StringUtil.isNotEmpty(suggestion)) {
                ConsoleUtil.printInfo("Suggestion: " + suggestion);
            }
        }
    }

    /**
     * Handles a validation error.
     *
     * @param fieldName the field that failed validation
     * @param errorMessage the error message
     */
    public static void handleValidationError(String fieldName, String errorMessage) {
        String message = "Validation error for " + fieldName + ": " + errorMessage;
        Logger.warn(message);
        ConsoleUtil.printWarning(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message
     */
    public static void displayError(String message) {
        Logger.error(message);
        ConsoleUtil.printError(message);
    }

    /**
     * Displays a warning message to the user.
     *
     * @param message the warning message
     */
    public static void displayWarning(String message) {
        Logger.warn(message);
        ConsoleUtil.printWarning(message);
    }

    /**
     * Displays an info message to the user.
     *
     * @param message the info message
     */
    public static void displayInfo(String message) {
        Logger.info(message);
        ConsoleUtil.printInfo(message);
    }

    /**
     * Displays a success message to the user.
     *
     * @param message the success message
     */
    public static void displaySuccess(String message) {
        Logger.info(message);
        ConsoleUtil.printSuccess(message);
    }

    /**
     * Wraps a risky operation with error handling.
     *
     * @param operation the operation to execute
     * @param errorMessage the error message to display if the operation fails
     * @return true if the operation succeeded, false otherwise
     */
    public static boolean safeExecute(RiskyOperation operation, String errorMessage) {
        try {
            operation.execute();
            return true;
        } catch (Exception e) {
            handleException(e, errorMessage);
            return false;
        }
    }

    /**
     * Functional interface for risky operations.
     */
    @FunctionalInterface
    public interface RiskyOperation {
        void execute() throws Exception;
    }
}
