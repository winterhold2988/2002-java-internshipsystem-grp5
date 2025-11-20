package code.cli;

import code.enums.ApplicationStatus;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterCriteria;
import code.filter.OpportunityFilterService;
import code.model.*;
import code.repository.*;
import code.service.InternshipService;
import code.service.StudentApplicationService;

import java.util.List;

/**
 * Menu for Student users.
 */
public class StudentMenu extends MenuBase {

    private final Student student;
    // SOLID - DIP: depend on abstraction that encapsulates student actions
    private final StudentApplicationService studentApplicationService;

    public StudentMenu(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipService internshipService,
            OpportunityFilterService opportunityFilterService,
            FilterStateManager filterStateManager,
            StudentApplicationService studentApplicationService,
            User currentUser) {
        super(userRepository, internshipRepository, applicationRepository,
                registrationRequestRepository, withdrawalRequestRepository, internshipService,
                opportunityFilterService, filterStateManager, currentUser);
        this.student = (Student) currentUser;
        this.studentApplicationService = studentApplicationService;
    }

    @Override
    public void display() {
        boolean running = true;

        while (running) {
            printStudentMenu();
            int choice = CLIUtil.readInt("Enter your choice: ", 0, 8);

            switch (choice) {
                case 1:
                    viewAvailableInternships();
                    break;
                case 2:
                    applyForInternship();
                    break;
                case 3:
                    viewMyApplications();
                    break;
                case 4:
                    requestWithdrawal();
                    break;
                case 5:
                    acceptOrDeclinePlacement();
                    break;
                case 6:
                    configureFilters();
                    break;
                case 7:
                    clearFilters();
                    break;
                case 8:
                    handleChangePassword();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    CLIUtil.displayError("Invalid choice. Please try again.");
                    CLIUtil.pause();
            }
        }
    }

    private void printStudentMenu() {
        printMenuHeader("Student Menu");
        String[] options = {
                "View Available Internships",
                "Apply for Internship",
                "View My Applications",
                "Request Application Withdrawal",
                "Accept/Decline Placement",
                "Configure Filters",
                "Clear Filters",
                "Change Password"
        };
        printMenuOptions(options);
    }

    private void viewAvailableInternships() {
        CLIUtil.printHeader("Available Internships");

        // Get student-eligible opportunities (year and major filtered)
        List<InternshipOpportunity> availableOpportunities = internshipService.listAvailableInternshipsForStudent(student);

        // Apply user's filter settings
        OpportunityFilterCriteria filterCriteria = filterStateManager.getFilterCriteria(currentUser.getId());
        List<InternshipOpportunity> filteredOpportunities = opportunityFilterService.filterOpportunities(availableOpportunities, filterCriteria);

        if (filterCriteria.hasActiveFilters()) {
            System.out.println(filterCriteria.toString());
            CLIUtil.printSeparator();
        }

        if (filteredOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No internships match your criteria.");
        } else {
            displayOpportunityList(filteredOpportunities);
        }

        CLIUtil.pause();
    }

    private void applyForInternship() {
        CLIUtil.printHeader("Apply for Internship");

        // Get student-eligible opportunities (year and major filtered)
        List<InternshipOpportunity> availableOpportunities = internshipService.listAvailableInternshipsForStudent(student);

        // Apply user's filter settings
        OpportunityFilterCriteria filterCriteria = filterStateManager.getFilterCriteria(currentUser.getId());
        List<InternshipOpportunity> filteredOpportunities = opportunityFilterService.filterOpportunities(availableOpportunities, filterCriteria);

        if (filteredOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No internships available to apply for.");
            CLIUtil.pause();
            return;
        }

        displayOpportunityList(filteredOpportunities);

        String oppId = CLIUtil.readString("\nEnter Internship ID to apply (or 'cancel'): ");

        if (oppId.equalsIgnoreCase("cancel")) {
            return;
        }

        InternshipOpportunity opportunity = studentApplicationService
                .findOpportunityById(oppId)
                .orElse(null);

        if (opportunity == null) {
            CLIUtil.displayError("Internship not found.");
            CLIUtil.pause();
            return;
        }

        // Check if already applied
        boolean alreadyApplied = studentApplicationService.hasExistingApplication(student, opportunity);

        if (alreadyApplied) {
            CLIUtil.displayError("You have already applied for this internship.");
            CLIUtil.pause();
            return;
        }

        // Create application
        InternshipApplication application = studentApplicationService.submitApplication(student, opportunity);

        CLIUtil.displaySuccess("Application submitted successfully! Application ID: " + application.getId());
        CLIUtil.pause();
    }

    private void viewMyApplications() {
        CLIUtil.printHeader("My Applications");

        List<InternshipApplication> myApplications = studentApplicationService.getApplications(student);

        if (myApplications.isEmpty()) {
            CLIUtil.displayInfo("You have not submitted any applications yet.");
        } else {
            for (InternshipApplication app : myApplications) {
                System.out.println("\nApplication ID: " + app.getId());
                System.out.println("Internship: " + app.getOpportunity().getTitle());
                System.out.println("Company: " + app.getOpportunity().getCompanyName());
                System.out.println("Status: " + app.getStatus());
                System.out.println("Submitted: " + app.getSubmittedAt());

                if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
                    System.out.println("Placement Accepted: " + (app.isPlacementAccepted() ? "Yes" : "No"));
                }

                CLIUtil.printSeparator();
            }
        }

        CLIUtil.pause();
    }

    private void requestWithdrawal() {
        CLIUtil.printHeader("Request Application Withdrawal");

        List<InternshipApplication> withdrawableApplications = studentApplicationService.getWithdrawableApplications(student);

        if (withdrawableApplications.isEmpty()) {
            CLIUtil.displayInfo("No applications available to withdraw.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Your applications (pending and successful):");
        for (int i = 0; i < withdrawableApplications.size(); i++) {
            InternshipApplication app = withdrawableApplications.get(i);
            String statusInfo = app.getStatus() == ApplicationStatus.SUCCESSFUL ? " [SUCCESSFUL]" : " [PENDING]";
            System.out.println((i + 1) + ". " + app.getId() + " - " +
                    app.getOpportunity().getTitle() + " at " +
                    app.getOpportunity().getCompanyName() + statusInfo);
        }

        int choice = CLIUtil.readInt("\nSelect application to withdraw (0 to cancel): ",
                0, withdrawableApplications.size());

        if (choice == 0) {
            return;
        }

        InternshipApplication selectedApp = withdrawableApplications.get(choice - 1);
        String reason = CLIUtil.readString("Enter reason for withdrawal: ");

        WithdrawalRequest request = studentApplicationService.requestWithdrawal(selectedApp, reason);

        CLIUtil.displaySuccess("Withdrawal request submitted. Request ID: " + request.getId());
        CLIUtil.pause();
    }

    private void acceptOrDeclinePlacement() {
        CLIUtil.printHeader("Accept/Decline Placement");

        List<InternshipApplication> successfulApplications = studentApplicationService
                .getApplicationsAwaitingDecision(student);

        if (successfulApplications.isEmpty()) {
            CLIUtil.displayInfo("No placements awaiting your decision.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Your successful applications:");
        for (int i = 0; i < successfulApplications.size(); i++) {
            InternshipApplication app = successfulApplications.get(i);
            System.out.println((i + 1) + ". " + app.getId() + " - " +
                    app.getOpportunity().getTitle() + " at " +
                    app.getOpportunity().getCompanyName());
        }

        int choice = CLIUtil.readInt("\nSelect placement to respond to (0 to cancel): ",
                0, successfulApplications.size());

        if (choice == 0) {
            return;
        }

        InternshipApplication selectedApp = successfulApplications.get(choice - 1);
        boolean accept = CLIUtil.readYesNo("Do you accept this placement?");

        if (accept) {
            studentApplicationService.acceptPlacement(selectedApp);
            CLIUtil.displaySuccess("Placement accepted! Congratulations!");
        } else {
            CLIUtil.displayInfo("Placement declined.");
        }

        CLIUtil.pause();
    }

    private void displayOpportunityList(List<InternshipOpportunity> opportunities) {
        for (InternshipOpportunity opp : opportunities) {
            System.out.println("\nID: " + opp.getId());
            System.out.println("Title: " + opp.getTitle());
            System.out.println("Company: " + opp.getCompanyName());
            System.out.println("Level: " + opp.getLevel());
            System.out.println("Preferred Major: " + opp.getPreferredMajor());
            System.out.println("Available Slots: " + (opp.getMaxSlots() - opp.getConfirmedSlots()));
            System.out.println("Closing Date: " + CLIUtil.formatDate(opp.getClosingDate()));
            System.out.println("Description: " + opp.getDescription());
            CLIUtil.printSeparator();
        }
    }

    @Override
    protected void printMenuOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back to Main Menu");
        CLIUtil.printSeparator();
    }
}
