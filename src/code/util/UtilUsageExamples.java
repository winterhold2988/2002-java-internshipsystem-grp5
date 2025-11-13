package code.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import code.util.exception.*;

/**
 * Comprehensive examples demonstrating the utility layer.
 * This class shows how to use all utility classes for common tasks.
 */
public class UtilUsageExamples {

    /**
     * Example 1: Date and time handling
     */
    public static void dateTimeExample() {
        ConsoleUtil.printHeader("Date and Time Example");
        
        // Parse dates
        String dateStr = "25/12/2024";
        DateTimeUtil.parseDate(dateStr).ifPresent(date -> {
            System.out.println("Parsed date: " + DateTimeUtil.formatDate(date));
            
            // Validate dates
            if (DateTimeUtil.isInFuture(date)) {
                System.out.println("This date is in the future");
            }
            
            // Calculate days
            long days = DateTimeUtil.daysBetween(LocalDate.now(), date);
            System.out.println("Days until: " + days);
        });
        
        // Check date ranges
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        LocalDate check = LocalDate.of(2024, 6, 15);
        
        if (DateTimeUtil.isWithinRange(check, start, end)) {
            System.out.println("Date is within range");
        }
    }

    /**
     * Example 2: String manipulation
     */
    public static void stringExample() {
        ConsoleUtil.printHeader("String Manipulation Example");
        
        // Safe operations
        String input = null;
        System.out.println("Is null or empty: " + StringUtil.isNullOrEmpty(input));
        
        input = "  john doe  ";
        String trimmed = StringUtil.trim(input);
        String capitalized = StringUtil.capitalizeWords(trimmed);
        System.out.println("Formatted name: " + capitalized);
        
        // Truncation
        String longText = "This is a very long text that needs to be truncated";
        System.out.println("Truncated: " + StringUtil.truncate(longText, 30));
        
        // Padding
        String id = "OPP001";
        System.out.println("Padded: " + StringUtil.padRight(id, 15));
        
        // Case conversion
        String camelCase = "helloWorld";
        System.out.println("Snake case: " + StringUtil.toSnakeCase(camelCase));
        
        String snakeCase = "hello_world";
        System.out.println("Camel case: " + StringUtil.toCamelCase(snakeCase));
        
        // Validation
        System.out.println("Is numeric '12345': " + StringUtil.isNumeric("12345"));
        System.out.println("Is alphabetic 'Hello': " + StringUtil.isAlphabetic("Hello"));
    }

    /**
     * Example 3: Console input validation
     */
    public static void inputValidationExample() {
        ConsoleUtil.printHeader("Input Validation Example");
        Scanner scanner = new Scanner(System.in);
        
        // Read integer within range
        System.out.print("Enter year (1-4): ");
        int year = InputValidator.readInt(scanner, 1, 4, "Invalid year");
        System.out.println("You entered: " + year);
        
        // Read non-empty string
        System.out.print("Enter your name: ");
        String name = InputValidator.readNonEmptyString(scanner, "Name");
        System.out.println("Hello, " + name);
        
        // Read email
        System.out.print("Enter email: ");
        String email = InputValidator.readEmail(scanner);
        System.out.println("Email: " + email);
        
        // Read date
        System.out.print("Enter date (dd/MM/yyyy): ");
        LocalDate date = InputValidator.readDate(scanner, "Date");
        System.out.println("Date: " + DateTimeUtil.formatDate(date));
        
        // Confirmation
        if (InputValidator.confirm(scanner, "Do you want to continue?")) {
            System.out.println("Continuing...");
        } else {
            System.out.println("Cancelled");
        }
    }

    /**
     * Example 4: Console formatting
     */
    public static void consoleFormattingExample() {
        // Header
        ConsoleUtil.printHeader("System Report");
        
        // Key-value pairs
        ConsoleUtil.printKeyValue("Total Students", "150");
        ConsoleUtil.printKeyValue("Total Opportunities", "45");
        ConsoleUtil.printKeyValue("Applications Pending", "23");
        
        ConsoleUtil.printSeparator();
        
        // Table
        String[] headers = {"ID", "Title", "Company", "Status"};
        List<String[]> rows = Arrays.asList(
            new String[]{"OPP0001", "Software Engineer", "TechCorp", "Open"},
            new String[]{"OPP0002", "Data Analyst", "DataWave", "Closed"},
            new String[]{"OPP0003", "UX Designer", "DesignHub", "Open"}
        );
        int[] widths = {10, 25, 20, 10};
        ConsoleUtil.printTable(headers, rows, widths);
        
        ConsoleUtil.printSeparator();
        
        // Lists
        List<String> items = Arrays.asList(
            "Computer Science",
            "Information Systems",
            "Data Science"
        );
        System.out.println("Available Majors:");
        ConsoleUtil.printBulletedList(items);
        
        ConsoleUtil.printSeparator();
        
        // Status messages
        ConsoleUtil.printSuccess("Application submitted successfully");
        ConsoleUtil.printWarning("Deadline approaching");
        ConsoleUtil.printInfo("New opportunities available");
        ConsoleUtil.printError("Invalid credentials");
        
        // Box
        ConsoleUtil.printBox("Important Notice");
        
        // Progress bar
        for (int i = 0; i <= 100; i += 10) {
            ConsoleUtil.printProgressBar(i, 100, 40);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    /**
     * Example 5: Error handling
     */
    public static void errorHandlingExample() {
        ConsoleUtil.printHeader("Error Handling Example");
        
        // Handle specific exception
        try {
            throw new ValidationException("Email format is invalid");
        } catch (Exception e) {
            ErrorHandler.handleException(e);
        }
        
        // Handle with custom message
        try {
            throw new DataNotFoundException("Student not found");
        } catch (Exception e) {
            ErrorHandler.handleException(e, "Failed to retrieve student data");
        }
        
        // Safe execution
        boolean success = ErrorHandler.safeExecute(() -> {
            // Simulate risky operation
            if (Math.random() > 0.5) {
                throw new BusinessRuleException("Cannot apply to closed opportunity");
            }
        }, "Failed to process application");
        
        if (success) {
            ErrorHandler.displaySuccess("Operation completed successfully");
        } else {
            ErrorHandler.displayWarning("Operation failed, please try again");
        }
        
        // Display messages
        ErrorHandler.displayInfo("Processing request...");
        ErrorHandler.displaySuccess("Request processed");
    }

    /**
     * Example 6: Logging
     */
    public static void loggingExample() {
        ConsoleUtil.printHeader("Logging Example");
        
        // Configure logger
        Logger.setLogLevel(Logger.LogLevel.DEBUG);
        Logger.setConsoleOutput(true);
        Logger.setFileOutput(true);
        
        // Log various levels
        Logger.debug("Debug message - detailed information");
        Logger.info("Info message - general information");
        Logger.warn("Warning message - potential issue");
        Logger.error("Error message - something went wrong");
        
        // Log with exception
        try {
            throw new RuntimeException("Sample error");
        } catch (Exception e) {
            Logger.error("An error occurred", e);
        }
        
        // Log method execution
        String className = "UserService";
        String methodName = "createUser";
        
        Logger.entering(className, methodName);
        // ... method logic ...
        Logger.exiting(className, methodName);
        
        // Log with parameters
        Logger.entering(className, "findUserById", "U2310001A");
        // ... method logic ...
        Logger.exiting(className, "findUserById", "User found");
    }

    /**
     * Example 7: Exception hierarchy
     */
    public static void exceptionExample() {
        ConsoleUtil.printHeader("Exception Hierarchy Example");
        
        // ValidationException
        try {
            String email = "invalid-email";
            if (!email.contains("@")) {
                throw new ValidationException("Invalid email format");
            }
        } catch (ValidationException e) {
            ErrorHandler.handleException(e);
        }
        
        // DataNotFoundException
        try {
            String userId = "U9999999Z";
            throw DataNotFoundException.forEntity("User", userId);
        } catch (DataNotFoundException e) {
            ErrorHandler.handleException(e);
        }
        
        // DuplicateEntryException
        try {
            String email = "john.doe@example.com";
            throw DuplicateEntryException.forEntity("User", email);
        } catch (DuplicateEntryException e) {
            ErrorHandler.handleException(e);
        }
        
        // UnauthorizedException
        try {
            throw UnauthorizedException.forAction("Delete opportunity", "CAREER_CENTER_STAFF");
        } catch (UnauthorizedException e) {
            ErrorHandler.handleException(e);
        }
        
        // BusinessRuleException
        try {
            throw new BusinessRuleException("Cannot withdraw application after deadline");
        } catch (BusinessRuleException e) {
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Example 8: Combined usage - Application submission flow
     */
    public static void applicationFlowExample() {
        ConsoleUtil.printHeader("Application Submission Flow");
        Scanner scanner = new Scanner(System.in);
        
        Logger.info("Starting application submission process");
        
        try {
            // Validate student
            System.out.print("Enter Student ID: ");
            String studentId = InputValidator.readNonEmptyString(scanner, "Student ID");
            
            if (!InputValidator.isValidIdFormat(studentId, "U")) {
                throw new ValidationException("Invalid student ID format. Expected: U1234567A");
            }
            
            Logger.debug("Student ID validated: " + studentId);
            
            // Select opportunity
            System.out.print("Enter Opportunity ID: ");
            String opportunityId = InputValidator.readNonEmptyString(scanner, "Opportunity ID");
            
            if (!InputValidator.isValidIdFormat(opportunityId, "OPP")) {
                throw new ValidationException("Invalid opportunity ID format. Expected: OPP0001");
            }
            
            Logger.debug("Opportunity ID validated: " + opportunityId);
            
            // Confirm
            ConsoleUtil.printSeparator();
            ConsoleUtil.printKeyValue("Student ID", studentId);
            ConsoleUtil.printKeyValue("Opportunity ID", opportunityId);
            ConsoleUtil.printSeparator();
            
            if (InputValidator.confirm(scanner, "Submit application?")) {
                // Process application
                Logger.info("Processing application submission");
                
                // Simulate processing
                ConsoleUtil.printInfo("Processing...");
                for (int i = 0; i <= 100; i += 20) {
                    ConsoleUtil.printProgressBar(i, 100, 40);
                    Thread.sleep(300);
                }
                
                ErrorHandler.displaySuccess("Application submitted successfully!");
                Logger.info("Application submitted: " + studentId + " -> " + opportunityId);
                
            } else {
                ErrorHandler.displayInfo("Application cancelled");
                Logger.info("Application submission cancelled by user");
            }
            
        } catch (ValidationException e) {
            ErrorHandler.handleException(e, "Validation failed");
        } catch (Exception e) {
            ErrorHandler.handleException(e, "Application submission failed");
        }
    }

    /**
     * Main method to run all examples
     */
    public static void main(String[] args) {
        ConsoleUtil.printHeader("Utility Layer Usage Examples", 100);
        ConsoleUtil.printEmptyLine();
        
        // Run non-interactive examples
        dateTimeExample();
        ConsoleUtil.printEmptyLines(2);
        
        stringExample();
        ConsoleUtil.printEmptyLines(2);
        
        consoleFormattingExample();
        ConsoleUtil.printEmptyLines(2);
        
        errorHandlingExample();
        ConsoleUtil.printEmptyLines(2);
        
        loggingExample();
        ConsoleUtil.printEmptyLines(2);
        
        exceptionExample();
        ConsoleUtil.printEmptyLines(2);
        
        // Interactive examples (commented out by default)
        // inputValidationExample();
        // applicationFlowExample();
        
        ConsoleUtil.printThickSeparator(100);
        ConsoleUtil.printSuccess("All examples completed!");
    }
}
