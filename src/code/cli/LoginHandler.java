package edu.ntu.ccds.sc2002.internship.cli;

import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.repository.UserRepository;

import java.util.Optional;

/**
 * Handles user authentication and login functionality.
 */
public class LoginHandler {

    private final UserRepository userRepository;

    public LoginHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
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

            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPassword().equals(password)) {
                    CLIUtil.displaySuccess("Login successful! Welcome, " + user.getName() + ".");
                    
                    // Check if user is using default password
                    if (password.equals("password")) {
                        CLIUtil.displayInfo("You are using the default password. Please change it for security.");
                        CLIUtil.pause();
                    }
                    
                    return user;
                } else {
                    attempts++;
                    CLIUtil.displayError("Invalid password. Attempts remaining: " + (maxAttempts - attempts));
                }
            } else {
                attempts++;
                CLIUtil.displayError("User ID not found. Attempts remaining: " + (maxAttempts - attempts));
            }

            if (attempts < maxAttempts) {
                CLIUtil.printBlankLine();
            }
        }

        CLIUtil.displayError("Maximum login attempts exceeded. Exiting...");
        return null;
    }
}
