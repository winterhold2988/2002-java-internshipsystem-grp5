package code.cli;

import code.model.User;
import code.repository.UserRepository;
import code.service.LoginService;

/**
 * Handles user authentication and login functionality.
 */
public class LoginHandler {

    private final LoginService loginService;

    public LoginHandler(UserRepository userRepository) {
        this.loginService = new LoginService(userRepository);
    }

    /**
     * Displays the login screen and authenticates the user.
     * 
     * @return The authenticated User, or null if login failed or user chose to exit
     */
    public User login() {
        CLIUtil.printBlankLine();
        CLIUtil.printHeader("Internship Placement Management System");
        System.out.println("Welcome! Please log in to continue.");
        CLIUtil.printSeparator();

        int maxAttempts = 3;
        int attempts = 0;

        while (attempts < maxAttempts) {
            String userId = CLIUtil.readString("User ID (or 'exit' to quit): ");

            if (userId.equalsIgnoreCase("exit")) {
                return null;
            }

            String password = CLIUtil.readString("Password: ");

            // Use LoginService for authentication
            User user = loginService.authentication(userId, password);

            if (user != null) {
                CLIUtil.displaySuccess("Login successful! Welcome, " + user.getName() + ".");

                // Check if user is using default password
                if (password.equals("password")) {
                    CLIUtil.displayInfo("You are using the default password. Please change it for security.");
                    CLIUtil.pause();
                }

                return user;
            } else {
                attempts++;
                CLIUtil.displayError("Invalid credentials (User ID or password incorrect). Attempts remaining: " + (maxAttempts - attempts));
            }

            if (attempts < maxAttempts) {
                CLIUtil.printBlankLine();
            }
        }

        CLIUtil.displayError("Maximum login attempts exceeded. Exiting...");
        return null;
    }
}
