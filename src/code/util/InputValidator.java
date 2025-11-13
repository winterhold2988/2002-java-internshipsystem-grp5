package code.util;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Utility class for validating and reading console input.
 * Provides methods to safely read different types of input with validation.
 */
public class InputValidator {

    private InputValidator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Reads and validates an integer from the scanner within a specified range.
     *
     * @param scanner the scanner to read from
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @param errorMessage the error message to display on invalid input
     * @return the validated integer
     */
    public static int readInt(Scanner scanner, int min, int max, String errorMessage) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println(errorMessage + " (Range: " + min + " - " + max + ")");
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage + " Please enter a valid number.");
            }
        }
    }

    /**
     * Reads and validates an integer from the scanner.
     *
     * @param scanner the scanner to read from
     * @param errorMessage the error message to display on invalid input
     * @return the validated integer
     */
    public static int readInt(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage + " Please enter a valid number.");
            }
        }
    }

    /**
     * Reads and validates a positive integer from the scanner.
     *
     * @param scanner the scanner to read from
     * @param errorMessage the error message to display on invalid input
     * @return the validated positive integer
     */
    public static int readPositiveInt(Scanner scanner, String errorMessage) {
        return readInt(scanner, 1, Integer.MAX_VALUE, errorMessage);
    }

    /**
     * Reads and validates a non-negative integer from the scanner.
     *
     * @param scanner the scanner to read from
     * @param errorMessage the error message to display on invalid input
     * @return the validated non-negative integer
     */
    public static int readNonNegativeInt(Scanner scanner, String errorMessage) {
        return readInt(scanner, 0, Integer.MAX_VALUE, errorMessage);
    }

    /**
     * Reads and validates a boolean from the scanner (y/n or yes/no).
     *
     * @param scanner the scanner to read from
     * @return true for yes/y, false for no/n
     */
    public static boolean readBoolean(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    /**
     * Reads a non-empty string from the scanner.
     *
     * @param scanner the scanner to read from
     * @param fieldName the name of the field being read
     * @return the validated non-empty string
     */
    public static String readNonEmptyString(Scanner scanner, String fieldName) {
        while (true) {
            String input = scanner.nextLine().trim();
            
            if (StringUtil.isNotEmpty(input)) {
                return input;
            } else {
                System.out.println(fieldName + " cannot be empty. Please try again.");
            }
        }
    }

    /**
     * Reads and validates a string matching a specific pattern.
     *
     * @param scanner the scanner to read from
     * @param pattern the regex pattern to match
     * @param fieldName the name of the field being read
     * @param errorMessage the error message to display on invalid input
     * @return the validated string
     */
    public static String readPattern(Scanner scanner, String pattern, String fieldName, String errorMessage) {
        while (true) {
            String input = scanner.nextLine().trim();
            
            if (input.matches(pattern)) {
                return input;
            } else {
                System.out.println(fieldName + ": " + errorMessage);
            }
        }
    }

    /**
     * Reads and validates an email address.
     *
     * @param scanner the scanner to read from
     * @return the validated email address
     */
    public static String readEmail(Scanner scanner) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return readPattern(scanner, emailPattern, "Email", 
                          "Invalid email format. Please enter a valid email address.");
    }

    /**
     * Reads and validates a date in dd/MM/yyyy format.
     *
     * @param scanner the scanner to read from
     * @param fieldName the name of the field being read
     * @return the validated LocalDate
     */
    public static LocalDate readDate(Scanner scanner, String fieldName) {
        while (true) {
            String input = scanner.nextLine().trim();
            
            if (DateTimeUtil.isValidDateFormat(input)) {
                return DateTimeUtil.parseDate(input).orElse(null);
            } else {
                System.out.println(fieldName + ": Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    /**
     * Reads and validates a future date.
     *
     * @param scanner the scanner to read from
     * @param fieldName the name of the field being read
     * @return the validated future LocalDate
     */
    public static LocalDate readFutureDate(Scanner scanner, String fieldName) {
        while (true) {
            LocalDate date = readDate(scanner, fieldName);
            
            if (DateTimeUtil.isInFuture(date)) {
                return date;
            } else {
                System.out.println(fieldName + " must be a future date. Please try again.");
            }
        }
    }

    /**
     * Reads and validates a date within a specific range.
     *
     * @param scanner the scanner to read from
     * @param fieldName the name of the field being read
     * @param startDate the minimum allowed date (inclusive)
     * @param endDate the maximum allowed date (inclusive)
     * @return the validated LocalDate
     */
    public static LocalDate readDateInRange(Scanner scanner, String fieldName, LocalDate startDate, LocalDate endDate) {
        while (true) {
            LocalDate date = readDate(scanner, fieldName);
            
            if (DateTimeUtil.isWithinRange(date, startDate, endDate)) {
                return date;
            } else {
                System.out.println(fieldName + " must be between " + 
                                 DateTimeUtil.formatDate(startDate) + " and " + 
                                 DateTimeUtil.formatDate(endDate));
            }
        }
    }

    /**
     * Reads a string with custom validation.
     *
     * @param scanner the scanner to read from
     * @param validator the validation predicate
     * @param fieldName the name of the field being read
     * @param errorMessage the error message to display on invalid input
     * @return the validated string
     */
    public static String readWithValidation(Scanner scanner, Predicate<String> validator, 
                                           String fieldName, String errorMessage) {
        while (true) {
            String input = scanner.nextLine().trim();
            
            if (validator.test(input)) {
                return input;
            } else {
                System.out.println(fieldName + ": " + errorMessage);
            }
        }
    }

    /**
     * Validates an ID format (e.g., STU0001, OPP0001).
     *
     * @param id the ID to validate
     * @param prefix the expected prefix (e.g., "STU", "OPP")
     * @return true if the ID is valid, false otherwise
     */
    public static boolean isValidIdFormat(String id, String prefix) {
        if (StringUtil.isNullOrEmpty(id)) {
            return false;
        }
        String pattern = "^" + prefix + "\\d{4}$";
        return id.matches(pattern);
    }

    /**
     * Validates a phone number format.
     *
     * @param phone the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (StringUtil.isNullOrEmpty(phone)) {
            return false;
        }
        // Matches formats like: 12345678, +65 1234 5678, (65) 1234-5678
        return phone.matches("^[+]?[(]?\\d{1,4}[)]?[-\\s]?[(]?\\d{1,4}[)]?[-\\s]?\\d{1,9}$");
    }

    /**
     * Validates a year value (1-4 for university years).
     *
     * @param year the year to validate
     * @return true if the year is valid (1-4), false otherwise
     */
    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 4;
    }

    /**
     * Validates a percentage value (0-100).
     *
     * @param percentage the percentage to validate
     * @return true if the percentage is valid, false otherwise
     */
    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0.0 && percentage <= 100.0;
    }

    /**
     * Confirms an action with the user (yes/no).
     *
     * @param scanner the scanner to read from
     * @param message the confirmation message
     * @return true if confirmed, false otherwise
     */
    public static boolean confirm(Scanner scanner, String message) {
        System.out.print(message + " (y/n): ");
        return readBoolean(scanner);
    }

    /**
     * Reads a choice from a menu of options.
     *
     * @param scanner the scanner to read from
     * @param minOption the minimum option number
     * @param maxOption the maximum option number
     * @return the selected option
     */
    public static int readMenuChoice(Scanner scanner, int minOption, int maxOption) {
        System.out.print("Enter your choice: ");
        return readInt(scanner, minOption, maxOption, "Invalid choice.");
    }
}
