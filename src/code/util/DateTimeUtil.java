package code.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Utility class for parsing and formatting dates and times.
 * Provides consistent date/time handling across the application.
 */
public class DateTimeUtil {

    // Standard date format: dd/MM/yyyy
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Standard date-time format: dd/MM/yyyy HH:mm:ss
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    // Display format for date-time: dd/MM/yyyy HH:mm
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateTimeUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Parses a date string in dd/MM/yyyy format.
     *
     * @param dateStr the date string to parse
     * @return an Optional containing the parsed LocalDate, or empty if parsing fails
     */
    public static Optional<LocalDate> parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(LocalDate.parse(dateStr.trim(), DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a date-time string in dd/MM/yyyy HH:mm:ss format.
     *
     * @param dateTimeStr the date-time string to parse
     * @return an Optional containing the parsed LocalDateTime, or empty if parsing fails
     */
    public static Optional<LocalDateTime> parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(LocalDateTime.parse(dateTimeStr.trim(), DATETIME_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Formats a LocalDate to dd/MM/yyyy format.
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats a LocalDateTime to dd/MM/yyyy HH:mm:ss format.
     *
     * @param dateTime the date-time to format
     * @return the formatted date-time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Formats a LocalDateTime for display (dd/MM/yyyy HH:mm).
     *
     * @param dateTime the date-time to format
     * @return the formatted date-time string
     */
    public static String formatDateTimeForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DISPLAY_DATETIME_FORMATTER);
    }

    /**
     * Gets the current date.
     *
     * @return the current LocalDate
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Gets the current date-time.
     *
     * @return the current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Checks if a date is in the past.
     *
     * @param date the date to check
     * @return true if the date is before today, false otherwise
     */
    public static boolean isInPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(getCurrentDate());
    }

    /**
     * Checks if a date is in the future.
     *
     * @param date the date to check
     * @return true if the date is after today, false otherwise
     */
    public static boolean isInFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(getCurrentDate());
    }

    /**
     * Checks if a date is today.
     *
     * @param date the date to check
     * @return true if the date is today, false otherwise
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(getCurrentDate());
    }

    /**
     * Checks if a date is within a range (inclusive).
     *
     * @param date the date to check
     * @param start the start of the range
     * @param end the end of the range
     * @return true if the date is within the range, false otherwise
     */
    public static boolean isWithinRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * Gets the number of days between two dates.
     *
     * @param start the start date
     * @param end the end date
     * @return the number of days between the dates
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Validates that a date string matches the expected format.
     *
     * @param dateStr the date string to validate
     * @return true if the string can be parsed as a date, false otherwise
     */
    public static boolean isValidDateFormat(String dateStr) {
        return parseDate(dateStr).isPresent();
    }

    /**
     * Validates that a date-time string matches the expected format.
     *
     * @param dateTimeStr the date-time string to validate
     * @return true if the string can be parsed as a date-time, false otherwise
     */
    public static boolean isValidDateTimeFormat(String dateTimeStr) {
        return parseDateTime(dateTimeStr).isPresent();
    }
}
