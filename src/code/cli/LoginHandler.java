package code.cli;

import code.model.User;
import code.model.CompanyRepresentative;
import code.model.RegistrationRequest;
import code.repository.UserRepository;
import code.repository.RegistrationRequestRepository;
import code.repository.ApplicationRepository;
import code.repository.InternshipRepository;
import code.repository.WithdrawalRequestRepository;
import code.service.LoginService;
import code.data.IDGenerator;

/**
 * Handles user authentication and login functionality.
 */
public class LoginHandler {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final IDGenerator idGenerator;

    public LoginHandler(UserRepository userRepository,
                       RegistrationRequestRepository registrationRequestRepository,
                       ApplicationRepository applicationRepository,
                       InternshipRepository internshipRepository,
                       WithdrawalRequestRepository withdrawalRequestRepository) {
        this.loginService = new LoginService(userRepository);
        this.userRepository = userRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.idGenerator = new IDGenerator(applicationRepository, internshipRepository,
                                          registrationRequestRepository, withdrawalRequestRepository);
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
        System.out.println("Type 'register' to register as a Company Representative");
        CLIUtil.printSeparator();

        int maxAttempts = 3;
        int attempts = 0;

        while (attempts < maxAttempts) {
            String userId = CLIUtil.readString("User ID (or 'exit' to quit): ");

            if (userId.equalsIgnoreCase("exit")) {
                return null;
            }
            
            if (userId.equalsIgnoreCase("register")) {
                registerCompanyRepresentative();
                continue;
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
    
    /**
     * Handles registration of new company representatives.
     */
    private void registerCompanyRepresentative() {
        CLIUtil.printBlankLine();
        CLIUtil.printHeader("Company Representative Registration");
        System.out.println("Please provide your details. Your registration will be reviewed by Career Center Staff.");
        CLIUtil.printSeparator();
        
        try {
            String email = CLIUtil.readString("Email Address (will be your User ID): ");
            
            // Check if email already exists
            if (userRepository.findById(email).isPresent()) {
                CLIUtil.displayError("This email is already registered. Please use a different email or try logging in.");
                CLIUtil.pause();
                return;
            }
            
            String name = CLIUtil.readString("Full Name: ");
            String companyName = CLIUtil.readString("Company Name: ");
            String department = CLIUtil.readString("Department: ");
            String position = CLIUtil.readString("Position: ");
            String password = CLIUtil.readString("Password: ");
            String confirmPassword = CLIUtil.readString("Confirm Password: ");
            
            if (!password.equals(confirmPassword)) {
                CLIUtil.displayError("Passwords do not match. Registration cancelled.");
                CLIUtil.pause();
                return;
            }
            
            // Create new company representative (not approved)
            CompanyRepresentative rep = new CompanyRepresentative(email, name, password);
            rep.setCompanyName(companyName);
            rep.setDepartment(department);
            rep.setPosition(position);
            rep.setApproved(false);
            
            // Save to repository
            userRepository.save(rep);
            
            // Create registration request
            String requestId = idGenerator.generateRequestId();
            RegistrationRequest request = new RegistrationRequest(requestId, rep);
            registrationRequestRepository.save(request);
            
            CLIUtil.displaySuccess("Registration submitted successfully!");
            System.out.println("Your registration will be reviewed by Career Center Staff.");
            System.out.println("You will be able to log in once your registration is approved.");
            System.out.println("Request ID: " + requestId);
            CLIUtil.pause();
            
        } catch (Exception e) {
            CLIUtil.displayError("Registration failed: " + e.getMessage());
            CLIUtil.pause();
        }
    }
}
