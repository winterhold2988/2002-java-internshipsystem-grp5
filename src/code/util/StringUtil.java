package code.util;

/**
 * Utility class for string operations and validations.
 * Provides common string manipulation and checking methods.
 */
public class StringUtil {

    private StringUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Checks if a string is null or empty (after trimming).
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty (after trimming).
     *
     * @param str the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * Trims a string, returning null if the input is null.
     *
     * @param str the string to trim
     * @return the trimmed string, or null if input is null
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * Trims a string, returning an empty string if the input is null.
     *
     * @param str the string to trim
     * @return the trimmed string, or empty string if input is null
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string, or null if input is null
     */
    public static String capitalize(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Capitalizes the first letter of each word in a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string, or null if input is null
     */
    public static String capitalizeWords(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(capitalize(words[i].toLowerCase()));
        }
        
        return result.toString();
    }

    /**
     * Truncates a string to a maximum length, adding ellipsis if truncated.
     *
     * @param str the string to truncate
     * @param maxLength the maximum length
     * @return the truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Pads a string to a certain length with spaces on the right.
     *
     * @param str the string to pad
     * @param length the desired length
     * @return the padded string
     */
    public static String padRight(String str, int length) {
        if (str == null) {
            str = "";
        }
        if (str.length() >= length) {
            return str;
        }
        return String.format("%-" + length + "s", str);
    }

    /**
     * Pads a string to a certain length with spaces on the left.
     *
     * @param str the string to pad
     * @param length the desired length
     * @return the padded string
     */
    public static String padLeft(String str, int length) {
        if (str == null) {
            str = "";
        }
        if (str.length() >= length) {
            return str;
        }
        return String.format("%" + length + "s", str);
    }

    /**
     * Repeats a string a certain number of times.
     *
     * @param str the string to repeat
     * @param count the number of times to repeat
     * @return the repeated string
     */
    public static String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Checks if a string contains only alphabetic characters.
     *
     * @param str the string to check
     * @return true if the string contains only letters, false otherwise
     */
    public static boolean isAlphabetic(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Checks if a string contains only numeric characters.
     *
     * @param str the string to check
     * @return true if the string contains only digits, false otherwise
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("\\d+");
    }

    /**
     * Checks if a string contains only alphanumeric characters.
     *
     * @param str the string to check
     * @return true if the string contains only letters and digits, false otherwise
     */
    public static boolean isAlphanumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }

    /**
     * Removes all whitespace from a string.
     *
     * @param str the string to process
     * @return the string without whitespace
     */
    public static String removeWhitespace(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * Converts a string to a safe filename by replacing invalid characters.
     *
     * @param str the string to convert
     * @return a safe filename string
     */
    public static String toSafeFilename(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    /**
     * Masks a string, showing only the last n characters.
     *
     * @param str the string to mask
     * @param visibleChars the number of characters to show at the end
     * @return the masked string
     */
    public static String mask(String str, int visibleChars) {
        if (str == null || str.length() <= visibleChars) {
            return str;
        }
        int maskLength = str.length() - visibleChars;
        return repeat("*", maskLength) + str.substring(maskLength);
    }

    /**
     * Compares two strings ignoring case and null-safe.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return true if the strings are equal (ignoring case), false otherwise
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Converts a camelCase or PascalCase string to snake_case.
     *
     * @param str the string to convert
     * @return the snake_case string
     */
    public static String toSnakeCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Converts a snake_case string to camelCase.
     *
     * @param str the string to convert
     * @return the camelCase string
     */
    public static String toCamelCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        
        String[] parts = str.split("_");
        StringBuilder result = new StringBuilder(parts[0].toLowerCase());
        
        for (int i = 1; i < parts.length; i++) {
            result.append(capitalize(parts[i].toLowerCase()));
        }
        
        return result.toString();
    }
}
