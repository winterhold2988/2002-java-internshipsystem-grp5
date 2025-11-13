package code.cli;

import code.enums.UserRole;
import code.model.User;
import code.repository.*;
import code.service.IntershipService;

/**
 * Main menu that routes users to their respective role-specific menus.
 */
public class MainMenu extends MenuBase {

    public MainMenu(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            IntershipService internshipService,
            User currentUser) {
        super(userRepository, internshipRepository, applicationRepository,
                registrationRequestRepository, withdrawalRequestRepository, internshipService, currentUser);
    }

    @Override
    public void display() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = CLIUtil.readInt("Enter your choice: ", 0, 2);

            switch (choice) {
                case 1:
                    navigateToRoleMenu();
                    break;
                case 2:
                    handleChangePassword();
                    break;
                case 0:
                    if (CLIUtil.readYesNo("Are you sure you want to logout?")) {
                        CLIUtil.displayInfo("Logging out...");
                        running = false;
                    }
                    break;
                default:
                    CLIUtil.displayError("Invalid choice. Please try again.");
                    CLIUtil.pause();
            }
        }
    }

    private void printMainMenu() {
        printMenuHeader("Main Menu");
        String[] options = {
                "Access " + getRoleMenuName(),
                "Change Password"
        };
        printMenuOptions(options);
    }

    private void navigateToRoleMenu() {
        UserRole role = currentUser.getRole();

        switch (role) {
            case STUDENT:
                StudentMenu studentMenu = new StudentMenu(
                        userRepository, internshipRepository, applicationRepository,
                        registrationRequestRepository, withdrawalRequestRepository, internshipService, currentUser);
                studentMenu.display();
                break;

            case COMPANY_REPRESENTATIVE:
                CompanyRepresentativeMenu companyMenu = new CompanyRepresentativeMenu(
                        userRepository, internshipRepository, applicationRepository,
                        registrationRequestRepository, withdrawalRequestRepository, internshipService, currentUser);
                companyMenu.display();
                break;

            case CAREER_CENTER_STAFF:
                CareerCenterStaffMenu staffMenu = new CareerCenterStaffMenu(
                        userRepository, internshipRepository, applicationRepository,
                        registrationRequestRepository, withdrawalRequestRepository, internshipService, currentUser);
                staffMenu.display();
                break;

            default:
                CLIUtil.displayError("Unknown user role.");
                CLIUtil.pause();
        }
    }

    private String getRoleMenuName() {
        switch (currentUser.getRole()) {
            case STUDENT:
                return "Student Menu";
            case COMPANY_REPRESENTATIVE:
                return "Company Representative Menu";
            case CAREER_CENTER_STAFF:
                return "Career Center Staff Menu";
            default:
                return "Role Menu";
        }
    }

    @Override
    protected void printMenuOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Logout");
        CLIUtil.printSeparator();
    }
}
