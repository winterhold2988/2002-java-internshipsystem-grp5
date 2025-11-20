package code.cli;

import code.enums.InternshipLevel;
import code.enums.OpportunityStatus;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterCriteria;
import code.filter.OpportunityFilterService;
import code.model.User;
import code.repository.*;
import code.service.InternshipService;

import java.time.LocalDate;

/**
 * Base class for all menu classes, providing common functionality and access to
 * repositories.
 */
public abstract class MenuBase {

    protected final UserRepository userRepository;
    protected final InternshipRepository internshipRepository;
    protected final ApplicationRepository applicationRepository;
    protected final RegistrationRequestRepository registrationRequestRepository;
    protected final WithdrawalRequestRepository withdrawalRequestRepository;
    protected final InternshipService internshipService;
    protected final User currentUser;
    protected final OpportunityFilterService opportunityFilterService;
    protected final FilterStateManager filterStateManager;

    protected MenuBase(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipService internshipService,
            OpportunityFilterService opportunityFilterService,
            FilterStateManager filterStateManager,
            User currentUser) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.withdrawalRequestRepository = withdrawalRequestRepository;
        this.internshipService = internshipService;
        this.opportunityFilterService = opportunityFilterService;
        this.filterStateManager = filterStateManager;
        this.currentUser = currentUser;
    }

    /**
     * Displays the menu and handles user interaction.
     */
    public abstract void display();

    /**
     * Prints the menu header with user information.
     */
    protected void printMenuHeader(String menuTitle) {
        CLIUtil.printBlankLine();
        CLIUtil.printHeader(menuTitle);
        System.out.println("Logged in as: " + currentUser.getName() + " (" + currentUser.getId() + ")");
        CLIUtil.printSeparator();
    }

    /**
     * Displays menu options.
     */
    protected void printMenuOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back");
        CLIUtil.printSeparator();
    }

    /**
     * Handles changing password.
     */
    protected void handleChangePassword() {
        CLIUtil.printHeader("Change Password");

        String currentPassword = CLIUtil.readString("Enter current password: ");

        if (!currentPassword.equals(currentUser.getPassword())) {
            CLIUtil.displayError("Current password is incorrect.");
            CLIUtil.pause();
            return;
        }

        String newPassword = CLIUtil.readString("Enter new password: ");
        String confirmPassword = CLIUtil.readString("Confirm new password: ");

        if (!newPassword.equals(confirmPassword)) {
            CLIUtil.displayError("Passwords do not match.");
            CLIUtil.pause();
            return;
        }

        if (newPassword.equals("password")) {
            CLIUtil.displayError("Cannot use default password 'password'. Please choose a different password.");
            CLIUtil.pause();
            return;
        }

        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
        CLIUtil.displaySuccess("Password changed successfully!");
        CLIUtil.pause();
    }

    /**
     * Interactive filter configuration UI for internship opportunities.
     * Allows users to set filters for status, major, level, and closing date.
     */
    protected void configureFilters() {
        CLIUtil.printHeader("Configure Opportunity Filters");

        OpportunityFilterCriteria currentCriteria = filterStateManager.getFilterCriteria(currentUser.getId());
        
        if (currentCriteria.hasActiveFilters()) {
            System.out.println("Current filters:");
            System.out.println(currentCriteria.toString());
            CLIUtil.printSeparator();
        }

        OpportunityFilterCriteria.Builder builder = OpportunityFilterCriteria.builder();
        builder.sortAlphabetically(true); // Always sort alphabetically

        // Status filter
        if (CLIUtil.readYesNo("Filter by Status?")) {
            System.out.println("\nSelect Status:");
            OpportunityStatus[] statuses = OpportunityStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }
            System.out.println("0. No filter");
            int statusChoice = CLIUtil.readInt("Enter choice: ", 0, statuses.length);
            if (statusChoice > 0) {
                builder.status(statuses[statusChoice - 1]);
            }
        }

        // Major filter
        // Commented out: Students already see only their major opportunities automatically
        // if (CLIUtil.readYesNo("Filter by Preferred Major?")) {
        //     String major = CLIUtil.readString("Enter preferred major: ");
        //     if (!major.trim().isEmpty()) {
        //         builder.preferredMajor(major);
        //     }
        // }

        // Level filter
        if (CLIUtil.readYesNo("Filter by Internship Level?")) {
            System.out.println("\nSelect Level:");
            InternshipLevel[] levels = InternshipLevel.values();
            for (int i = 0; i < levels.length; i++) {
                System.out.println((i + 1) + ". " + levels[i]);
            }
            System.out.println("0. No filter");
            int levelChoice = CLIUtil.readInt("Enter choice: ", 0, levels.length);
            if (levelChoice > 0) {
                builder.level(levels[levelChoice - 1]);
            }
        }

        // Closing date filter
        if (CLIUtil.readYesNo("Filter by Closing Date (before a specific date)?")) {
            LocalDate closingDate = CLIUtil.readDate("Enter closing date");
            builder.closingDateBefore(closingDate);
        }

        OpportunityFilterCriteria newCriteria = builder.build();
        filterStateManager.updateFilterCriteria(currentUser.getId(), newCriteria);

        CLIUtil.displaySuccess("Filters applied successfully!");
        if (newCriteria.hasActiveFilters()) {
            System.out.println(newCriteria.toString());
        } else {
            System.out.println("No filters active. Showing all opportunities (sorted alphabetically).");
        }
        CLIUtil.pause();
    }

    /**
     * Clears all active filters for the current user.
     */
    protected void clearFilters() {
        filterStateManager.clearFilters(currentUser.getId());
        CLIUtil.displaySuccess("All filters cleared. Showing all opportunities (sorted alphabetically).");
        CLIUtil.pause();
    }
}
