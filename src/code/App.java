package code;

import code.cli.CLIUtil;
import code.cli.DefaultRoleMenuFactory;
import code.cli.LoginHandler;
import code.cli.MainMenu;
import code.cli.RoleMenuFactory;
import code.config.AppConfig;
import code.model.*;
import code.util.*;

/**
 * Entry point for the Internship Placement Management System.
 * 
 * This application serves as a centralized hub for:
 * - Students (ID format: U1234567A)
 * - Company Representatives (ID: company email)
 * - Career Center Staff (ID: NTU account)
 * 
 * System Features:
 * 1. User Authentication & Management
 * 2. Internship Opportunity Creation & Approval
 * 3. Student Applications (max 3 concurrent)
 * 4. Placement Confirmation & Withdrawal
 * 5. Comprehensive Reporting & Filtering
 * 
 * @version 1.0
 * @since 2025-11-13
 */
public final class App {

    private static AppConfig config;
    private static RoleMenuFactory roleMenuFactory;

    public static void main(String[] args) {
        try {
            // Configure logging
            initializeLogging();

            Logger.info("=== Internship Placement Management System Starting ===");

            // Initialize application configuration
            config = new AppConfig();
            Logger.info("Application configuration initialized");
            roleMenuFactory = new DefaultRoleMenuFactory(
                    config.getUserRepository(),
                    config.getInternshipRepository(),
                    config.getApplicationRepository(),
                    config.getRegistrationRequestRepository(),
                    config.getWithdrawalRequestRepository(),
                    config.getInternshipService(),
                    config.getStudentApplicationService(),
                    config.getOpportunityFilterService(),
                    config.getFilterStateManager());

            // Display welcome message
            displayWelcomeBanner();

            // Display data loading summary
            displayDataSummary();

            // Main application loop
            runApplicationLoop();

            // Save data before exit
            saveDataOnExit();

            // Exit message
            displayExitMessage();

            Logger.info("=== Internship Placement Management System Stopped ===");

        } catch (Exception e) {
            ErrorHandler.handleException(e, "Critical error during application startup");
            Logger.error("Application failed to start", e);
        } finally {
            // Clean up
            CLIUtil.closeScanner();
        }
    }

    /**
     * Initializes the logging system.
     */
    private static void initializeLogging() {
        Logger.setLogLevel(Logger.LogLevel.INFO);
        Logger.setConsoleOutput(false); // Don't clutter console
        Logger.setFileOutput(true);
    }

    /**
     * Runs the main application loop.
     */
    private static void runApplicationLoop() {
        boolean running = true;

        while (running) {
            try {
                // Login handler
                LoginHandler loginHandler = new LoginHandler(
                    config.getUserRepository(),
                    config.getRegistrationRequestRepository(),
                    config.getApplicationRepository(),
                    config.getInternshipRepository(),
                    config.getWithdrawalRequestRepository());
                User currentUser = loginHandler.login();

                if (currentUser == null) {
                    // User chose to exit or login failed
                    Logger.info("User chose to exit the application");
                    running = false;
                } else {
                    Logger.info("User logged in: " + currentUser.getId() + " (" + currentUser.getRole() + ")");

                    // Show main menu based on user role
                    MainMenu mainMenu = new MainMenu(
                            config.getUserRepository(),
                            config.getInternshipRepository(),
                            config.getApplicationRepository(),
                            config.getRegistrationRequestRepository(),
                            config.getWithdrawalRequestRepository(),
                            config.getInternshipService(),
                            config.getOpportunityFilterService(),
                            config.getFilterStateManager(),
                            roleMenuFactory,
                            currentUser);
                    mainMenu.display();

                    Logger.info("User logged out: " + currentUser.getId());
                }
            } catch (Exception e) {
                ErrorHandler.handleException(e, "Error during application loop");
                Logger.error("Application loop error", e);
            }
        }
    }

    /**
     * Displays data loading summary.
     */
    private static void displayDataSummary() {
        ConsoleUtil.printSeparator();
        ConsoleUtil.printInfo("System initialized successfully!");

        // Get statistics
        long studentCount = config.getUserRepository().findAll().stream()
                .filter(u -> u.getRole() == code.enums.UserRole.STUDENT).count();
        long staffCount = config.getUserRepository().findAll().stream()
                .filter(u -> u.getRole() == code.enums.UserRole.CAREER_CENTER_STAFF).count();
        long repCount = config.getUserRepository().findAll().stream()
                .filter(u -> u.getRole() == code.enums.UserRole.COMPANY_REPRESENTATIVE).count();

        ConsoleUtil.printKeyValue("Students loaded", String.valueOf(studentCount));
        ConsoleUtil.printKeyValue("Staff loaded", String.valueOf(staffCount));
        ConsoleUtil.printKeyValue("Company Reps loaded", String.valueOf(repCount));

        ConsoleUtil.printSeparator();
        ConsoleUtil.printEmptyLine();
    }

    /**
     * Saves all data before exit.
     */
    private static void saveDataOnExit() {
        try {
            Logger.info("Saving application data...");
            config.getDataPersistenceManager().exportAll("src/resources/data");
            Logger.info("Data saved successfully");
        } catch (Exception e) {
            Logger.error("Failed to save data on exit", e);
            ErrorHandler.displayWarning("Could not save all data. Some changes may be lost.");
        }
    }

    /**
     * Displays exit message.
     */
    private static void displayExitMessage() {
        ConsoleUtil.printEmptyLine();
        ConsoleUtil.printHeader("Thank you for using the Internship Placement Management System");
        ConsoleUtil.printBox(java.util.Arrays.asList(
                "System developed by Group 5",
                "SC/CE/CZ2002 - Object-Oriented Design & Programming",
                "Nanyang Technological University - 2025"));
        ConsoleUtil.printSuccess("Goodbye!");
        ConsoleUtil.printEmptyLine();
    }

    /**
     * Displays the welcome banner.
     */
    private static void displayWelcomeBanner() {
        ConsoleUtil.printEmptyLine();
        ConsoleUtil.printThickSeparator(80);
        System.out.println("   _____ _____ ___   ___   ___  ___  ");
        System.out.println("  / ____/ ____|__ \\ / _ \\ / _ \\|__ \\ ");
        System.out.println(" | (___| |       ) | | | | | | |  ) |");
        System.out.println("  \\___ \\ |      / /| | | | | | | / / ");
        System.out.println("  ____) | |____ / /_| |_| | |_| |/ /_ ");
        System.out.println(" |_____/ \\_____|____|\\___/ \\___/|____|");
        System.out.println();
        System.out.println(StringUtil.padLeft("Internship Placement Management System", 60));
        System.out.println(StringUtil.padLeft("NTU - Group 5 - 2025", 60));
        ConsoleUtil.printThickSeparator(80);
        ConsoleUtil.printEmptyLine();
    }
}
