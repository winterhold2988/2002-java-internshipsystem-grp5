package code.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class for common CLI operations such as input reading and validation.
 */
public final class CLIUtil {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CLIUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Prints a separator line for visual clarity.
     */
    public static void printSeparator() {
        System.out.println("================================================================================");
    }

    /**
     * Prints a header with separators.
     */
    public static void printHeader(String title) {
        printSeparator();
        System.out.println(title);
        printSeparator();
    }

    /**
     * Prints a blank line.
     */
    public static void printBlankLine() {
        System.out.println();
    }

    /**
     * Reads a non-empty string from the user.
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    /**
     * Reads a string that can be empty.
     */
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads an integer within a specified range.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads an integer without range restriction.
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a positive integer.
     */
    public static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    /**
     * Reads a date in dd/MM/yyyy format.
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd/MM/yyyy): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    /**
     * Reads a yes/no confirmation.
     */
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    /**
     * Displays a success message.
     */
    public static void displaySuccess(String message) {
        System.out.println("✓ " + message);
    }

    /**
     * Displays an error message.
     */
    public static void displayError(String message) {
        System.out.println("✗ ERROR: " + message);
    }

    /**
     * Displays an info message.
     */
    public static void displayInfo(String message) {
        System.out.println("ℹ " + message);
    }

    /**
     * Pauses execution until user presses Enter.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Formats a date for display.
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "N/A";
    }

    /**
     * Gets the global scanner instance.
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Closes the scanner (call only on application exit).
     */
    public static void closeScanner() {
        scanner.close();
    }
}
