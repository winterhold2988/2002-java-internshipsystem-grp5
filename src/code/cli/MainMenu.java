package code.cli;

import code.filter.FilterStateManager;
import code.filter.OpportunityFilterService;
import code.model.User;
import code.repository.*;
import code.service.InternshipService;

/**
 * Main menu that routes users to their respective role-specific menus.
 */
public class MainMenu extends MenuBase {

    private final RoleMenuFactory roleMenuFactory;

    public MainMenu(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipService internshipService,
            OpportunityFilterService opportunityFilterService,
            FilterStateManager filterStateManager,
            RoleMenuFactory roleMenuFactory,
            User currentUser) {
        super(userRepository, internshipRepository, applicationRepository,
                registrationRequestRepository, withdrawalRequestRepository, internshipService,
                opportunityFilterService, filterStateManager, currentUser);
        this.roleMenuFactory = roleMenuFactory;
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
        MenuBase roleMenu = roleMenuFactory.createMenu(currentUser);
        if (roleMenu == null) {
            CLIUtil.displayError("Unknown user role.");
            CLIUtil.pause();
            return;
        }

        roleMenu.display();
    }

    private String getRoleMenuName() {
        return roleMenuFactory.getMenuName(currentUser.getRole());
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
