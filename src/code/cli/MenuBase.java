package code.cli;

import code.model.User;
import code.repository.*;
import code.service.IntershipService;

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
    protected final IntershipService internshipService;
    protected final User currentUser;

    protected MenuBase(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            IntershipService internshipService,
            User currentUser) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.withdrawalRequestRepository = withdrawalRequestRepository;
        this.internshipService = internshipService;
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
}
