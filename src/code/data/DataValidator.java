package code.data;

import java.util.regex.Pattern;

/**
 * DataValidator provides utility methods for validating data before it is
 * saved to repositories or exported to files.
 */
public class DataValidator {

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Student ID pattern (e.g., U2310001A)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Z]$");

    // Staff ID pattern (e.g., sng001)
    private static final Pattern STAFF_ID_PATTERN = Pattern.compile("^[a-z]+\\d{3}$");

    /**
     * Validates an email address.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates a student ID.
     *
     * @param studentId the student ID to validate
     * @return true if the student ID is valid, false otherwise
     */
    public static boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return STUDENT_ID_PATTERN.matcher(studentId).matches();
    }

    /**
     * Validates a staff ID.
     *
     * @param staffId the staff ID to validate
     * @return true if the staff ID is valid, false otherwise
     */
    public static boolean isValidStaffId(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            return false;
        }
        return STAFF_ID_PATTERN.matcher(staffId).matches();
    }

    /**
     * Validates that a string is not null or empty.
     *
     * @param value the string to validate
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates that a year of study is within valid range (1-4).
     *
     * @param year the year to validate
     * @return true if the year is valid, false otherwise
     */
    public static boolean isValidYearOfStudy(int year) {
        return year >= 1 && year <= 4;
    }

    /**
     * Validates that a value is positive.
     *
     * @param value the value to validate
     * @return true if the value is positive, false otherwise
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates that a value is non-negative.
     *
     * @param value the value to validate
     * @return true if the value is non-negative, false otherwise
     */
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }
}
