package edu.ntu.ccds.sc2002.internship;

import edu.ntu.ccds.sc2002.internship.cli.CLIUtil;
import edu.ntu.ccds.sc2002.internship.cli.LoginHandler;
import edu.ntu.ccds.sc2002.internship.cli.MainMenu;
import edu.ntu.ccds.sc2002.internship.model.*;
import edu.ntu.ccds.sc2002.internship.repository.*;

/**
 * Entry point for the Internship Placement Management System.
 */
public final class App {

    public static void main(String[] args) {
        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        InternshipRepository internshipRepository = new InternshipRepository();
        ApplicationRepository applicationRepository = new ApplicationRepository();
        RegistrationRequestRepository registrationRequestRepository = new RegistrationRequestRepository();
        WithdrawalRequestRepository withdrawalRequestRepository = new WithdrawalRequestRepository();

        // Bootstrap sample data
        bootstrapData(userRepository, registrationRequestRepository);

        // Display welcome message
        displayWelcomeBanner();

        // Main application loop
        boolean running = true;
        
        while (running) {
            // Login handler
            LoginHandler loginHandler = new LoginHandler(userRepository);
            User currentUser = loginHandler.login();

            if (currentUser == null) {
                // User chose to exit or login failed
                running = false;
            } else {
                // Show main menu
                MainMenu mainMenu = new MainMenu(
                    userRepository,
                    internshipRepository,
                    applicationRepository,
                    registrationRequestRepository,
                    withdrawalRequestRepository,
                    currentUser
                );
                mainMenu.display();
            }
        }

        // Exit message
        CLIUtil.printBlankLine();
        CLIUtil.printHeader("Thank you for using the Internship Placement Management System");
        System.out.println("Goodbye!");
        CLIUtil.printBlankLine();
        
        // Clean up
        CLIUtil.closeScanner();
    }

    /**
     * Displays the welcome banner.
     */
    private static void displayWelcomeBanner() {
        CLIUtil.printBlankLine();
        CLIUtil.printSeparator();
        System.out.println("   _____ _____ ___   ___   ___  ___  ");
        System.out.println("  / ____/ ____|__ \\ / _ \\ / _ \\|__ \\ ");
        System.out.println(" | (___| |       ) | | | | | | |  ) |");
        System.out.println("  \\___ \\ |      / /| | | | | | | / / ");
        System.out.println("  ____) | |____ / /_| |_| | |_| |/ /_ ");
        System.out.println(" |_____/ \\_____|____|\\___/ \\___/|____|");
        System.out.println();
        System.out.println("    Internship Placement Management System");
        System.out.println("           NTU - Group 5 - 2025");
        CLIUtil.printSeparator();
    }

    /**
     * Bootstraps sample data for testing.
     */
    private static void bootstrapData(UserRepository userRepository, 
                                     RegistrationRequestRepository registrationRequestRepository) {
        // Create sample students
        Student student1 = new Student("S001", "Alice Tan", "password", 2, "Computer Science");
        Student student2 = new Student("S002", "Bob Lee", "password", 3, "Business Analytics");
        Student student3 = new Student("S003", "Charlie Wong", "password", 1, "Data Science");
        
        userRepository.save(student1);
        userRepository.save(student2);
        userRepository.save(student3);

        // Create sample career center staff
        CareerCenterStaff staff1 = new CareerCenterStaff("STAFF001", "Dr. Sarah Chen", "password", "Career Services");
        CareerCenterStaff staff2 = new CareerCenterStaff("STAFF002", "Mr. David Lim", "password", "Student Affairs");
        
        userRepository.save(staff1);
        userRepository.save(staff2);

        // Create sample company representatives (some approved, some pending)
        CompanyRepresentative rep1 = new CompanyRepresentative("CR001", "John Smith", "password");
        rep1.setCompanyName("TechCorp Pte Ltd");
        rep1.setDepartment("HR");
        rep1.setPosition("HR Manager");
        rep1.setApproved(true);
        userRepository.save(rep1);

        CompanyRepresentative rep2 = new CompanyRepresentative("CR002", "Mary Johnson", "password");
        rep2.setCompanyName("InnovateLabs");
        rep2.setDepartment("Talent Acquisition");
        rep2.setPosition("Recruitment Lead");
        rep2.setApproved(true);
        userRepository.save(rep2);

        CompanyRepresentative rep3 = new CompanyRepresentative("CR003", "Peter Tan", "password");
        rep3.setCompanyName("StartupHub");
        rep3.setDepartment("People & Culture");
        rep3.setPosition("Internship Coordinator");
        rep3.setApproved(false); // Pending approval
        userRepository.save(rep3);

        // Create registration request for pending representative
        RegistrationRequest request = new RegistrationRequest("REG001", rep3);
        registrationRequestRepository.save(request);

        System.out.println("\n✓ Sample data loaded successfully!");
        System.out.println("\nSample login credentials:");
        System.out.println("  Student: S001, S002, S003 (password: password)");
        System.out.println("  Staff: STAFF001, STAFF002 (password: password)");
        System.out.println("  Company Rep: CR001, CR002 (approved), CR003 (pending approval)");
        System.out.println("  All passwords: password\n");
    }
}
