package code.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Simple logging utility for the internship management system.
 * Supports multiple log levels and writes to both console and file.
 */
public class Logger {

    /**
     * Log levels.
     */
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    private static final String LOG_FILE = "internship_system.log";
    private static LogLevel currentLogLevel = LogLevel.INFO;
    private static boolean consoleOutput = true;
    private static boolean fileOutput = true;

    private Logger() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets the current log level.
     *
     * @param level the log level
     */
    public static void setLogLevel(LogLevel level) {
        currentLogLevel = level;
    }

    /**
     * Enables or disables console output.
     *
     * @param enabled true to enable, false to disable
     */
    public static void setConsoleOutput(boolean enabled) {
        consoleOutput = enabled;
    }

    /**
     * Enables or disables file output.
     *
     * @param enabled true to enable, false to disable
     */
    public static void setFileOutput(boolean enabled) {
        fileOutput = enabled;
    }

    /**
     * Logs a debug message.
     *
     * @param message the message to log
     */
    public static void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    /**
     * Logs an info message.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    /**
     * Logs an error message with exception.
     *
     * @param message the message to log
     * @param throwable the exception
     */
    public static void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    /**
     * Logs a message at the specified level.
     *
     * @param level the log level
     * @param message the message to log
     * @param throwable the exception (optional)
     */
    private static void log(LogLevel level, String message, Throwable throwable) {
        if (level.ordinal() < currentLogLevel.ordinal()) {
            return; // Skip if below current log level
        }

        String timestamp = DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDateTime());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);

        // Console output
        if (consoleOutput) {
            if (level == LogLevel.ERROR) {
                System.err.println(logMessage);
                if (throwable != null) {
                    throwable.printStackTrace(System.err);
                }
            } else {
                System.out.println(logMessage);
            }
        }

        // File output
        if (fileOutput) {
            writeToFile(logMessage, throwable);
        }
    }

    /**
     * Writes a log message to file.
     *
     * @param message the message to write
     * @param throwable the exception (optional)
     */
    private static void writeToFile(String message, Throwable throwable) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println(message);
            
            if (throwable != null) {
                throwable.printStackTrace(pw);
            }
            
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    /**
     * Logs method entry.
     *
     * @param className the class name
     * @param methodName the method name
     */
    public static void entering(String className, String methodName) {
        debug("Entering " + className + "." + methodName + "()");
    }

    /**
     * Logs method exit.
     *
     * @param className the class name
     * @param methodName the method name
     */
    public static void exiting(String className, String methodName) {
        debug("Exiting " + className + "." + methodName + "()");
    }

    /**
     * Logs method entry with parameters.
     *
     * @param className the class name
     * @param methodName the method name
     * @param params the method parameters
     */
    public static void entering(String className, String methodName, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("Entering ").append(className).append(".").append(methodName).append("(");
        
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(params[i]);
        }
        
        sb.append(")");
        debug(sb.toString());
    }

    /**
     * Logs method exit with return value.
     *
     * @param className the class name
     * @param methodName the method name
     * @param returnValue the return value
     */
    public static void exiting(String className, String methodName, Object returnValue) {
        debug("Exiting " + className + "." + methodName + "() with return value: " + returnValue);
    }
}
