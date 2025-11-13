package code.util;

import java.util.List;

/**
 * Utility class for console formatting and display.
 * Provides methods for creating formatted console output.
 */
public class ConsoleUtil {

    private static final int DEFAULT_WIDTH = 80;
    private static final String BORDER_CHAR = "=";
    private static final String LINE_CHAR = "-";

    private ConsoleUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Prints a header with a border.
     *
     * @param title the header title
     */
    public static void printHeader(String title) {
        printHeader(title, DEFAULT_WIDTH);
    }

    /**
     * Prints a header with a border and custom width.
     *
     * @param title the header title
     * @param width the width of the header
     */
    public static void printHeader(String title, int width) {
        String border = StringUtil.repeat(BORDER_CHAR, width);
        System.out.println(border);
        System.out.println(centerText(title, width));
        System.out.println(border);
    }

    /**
     * Prints a section separator.
     */
    public static void printSeparator() {
        printSeparator(DEFAULT_WIDTH);
    }

    /**
     * Prints a section separator with custom width.
     *
     * @param width the width of the separator
     */
    public static void printSeparator(int width) {
        System.out.println(StringUtil.repeat(LINE_CHAR, width));
    }

    /**
     * Prints a thick separator.
     */
    public static void printThickSeparator() {
        printThickSeparator(DEFAULT_WIDTH);
    }

    /**
     * Prints a thick separator with custom width.
     *
     * @param width the width of the separator
     */
    public static void printThickSeparator(int width) {
        System.out.println(StringUtil.repeat(BORDER_CHAR, width));
    }

    /**
     * Centers text within a given width.
     *
     * @param text the text to center
     * @param width the total width
     * @return the centered text
     */
    public static String centerText(String text, int width) {
        if (text == null || text.length() >= width) {
            return text;
        }
        
        int padding = (width - text.length()) / 2;
        String leftPad = StringUtil.repeat(" ", padding);
        String rightPad = StringUtil.repeat(" ", width - text.length() - padding);
        
        return leftPad + text + rightPad;
    }

    /**
     * Prints a success message.
     *
     * @param message the success message
     */
    public static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    /**
     * Prints an error message.
     *
     * @param message the error message
     */
    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Prints a warning message.
     *
     * @param message the warning message
     */
    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    /**
     * Prints an info message.
     *
     * @param message the info message
     */
    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Prints a formatted table with headers and rows.
     *
     * @param headers the column headers
     * @param rows the data rows
     * @param columnWidths the width of each column
     */
    public static void printTable(String[] headers, List<String[]> rows, int[] columnWidths) {
        if (headers.length != columnWidths.length) {
            throw new IllegalArgumentException("Number of headers must match number of column widths");
        }

        // Print top border
        printTableBorder(columnWidths);

        // Print headers
        printTableRow(headers, columnWidths);

        // Print separator
        printTableBorder(columnWidths);

        // Print rows
        for (String[] row : rows) {
            if (row.length != headers.length) {
                throw new IllegalArgumentException("Row length must match number of headers");
            }
            printTableRow(row, columnWidths);
        }

        // Print bottom border
        printTableBorder(columnWidths);
    }

    /**
     * Prints a table border.
     *
     * @param columnWidths the width of each column
     */
    private static void printTableBorder(int[] columnWidths) {
        System.out.print("+");
        for (int width : columnWidths) {
            System.out.print(StringUtil.repeat("-", width + 2) + "+");
        }
        System.out.println();
    }

    /**
     * Prints a table row.
     *
     * @param cells the cell values
     * @param columnWidths the width of each column
     */
    private static void printTableRow(String[] cells, int[] columnWidths) {
        System.out.print("|");
        for (int i = 0; i < cells.length; i++) {
            String cell = cells[i] == null ? "" : cells[i];
            System.out.print(" " + StringUtil.padRight(cell, columnWidths[i]) + " |");
        }
        System.out.println();
    }

    /**
     * Prints a numbered list.
     *
     * @param items the list items
     */
    public static void printNumberedList(List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }

    /**
     * Prints a bulleted list.
     *
     * @param items the list items
     */
    public static void printBulletedList(List<String> items) {
        for (String item : items) {
            System.out.println("• " + item);
        }
    }

    /**
     * Prints a key-value pair.
     *
     * @param key the key
     * @param value the value
     */
    public static void printKeyValue(String key, String value) {
        printKeyValue(key, value, 20);
    }

    /**
     * Prints a key-value pair with custom key width.
     *
     * @param key the key
     * @param value the value
     * @param keyWidth the width for the key column
     */
    public static void printKeyValue(String key, String value, int keyWidth) {
        System.out.println(StringUtil.padRight(key + ":", keyWidth) + " " + value);
    }

    /**
     * Clears the console (platform-independent simulation).
     */
    public static void clearConsole() {
        // Print multiple blank lines to simulate clearing
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Waits for user to press Enter.
     */
    public static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * Prints a progress bar.
     *
     * @param current the current progress
     * @param total the total progress
     * @param width the width of the progress bar
     */
    public static void printProgressBar(int current, int total, int width) {
        int percentage = (int) ((current * 100.0) / total);
        int filled = (int) ((current * width) / total);
        
        String bar = "[" + 
                    StringUtil.repeat("=", filled) + 
                    StringUtil.repeat(" ", width - filled) + 
                    "] " + percentage + "%";
        
        System.out.print("\r" + bar);
        
        if (current == total) {
            System.out.println();
        }
    }

    /**
     * Prints a box around text.
     *
     * @param text the text to box
     */
    public static void printBox(String text) {
        int width = text.length() + 4;
        String border = StringUtil.repeat("*", width);
        
        System.out.println(border);
        System.out.println("* " + text + " *");
        System.out.println(border);
    }

    /**
     * Prints multiple lines in a box.
     *
     * @param lines the lines to box
     */
    public static void printBox(List<String> lines) {
        int maxLength = lines.stream()
                             .mapToInt(String::length)
                             .max()
                             .orElse(0);
        
        int width = maxLength + 4;
        String border = StringUtil.repeat("*", width);
        
        System.out.println(border);
        for (String line : lines) {
            System.out.println("* " + StringUtil.padRight(line, maxLength) + " *");
        }
        System.out.println(border);
    }

    /**
     * Formats a menu option.
     *
     * @param number the option number
     * @param description the option description
     * @return the formatted menu option
     */
    public static String formatMenuOption(int number, String description) {
        return String.format("[%d] %s", number, description);
    }

    /**
     * Prints an empty line.
     */
    public static void printEmptyLine() {
        System.out.println();
    }

    /**
     * Prints multiple empty lines.
     *
     * @param count the number of empty lines
     */
    public static void printEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }
}
