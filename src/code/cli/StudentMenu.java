package code.cli;

import code.enums.ApplicationStatus;
import code.model.*;
import code.repository.*;
import code.service.IntershipService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Menu for Student users.
 */
public class StudentMenu extends MenuBase {

    private final Student student;

    public StudentMenu(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            IntershipService internshipService,
            User currentUser) {
        super(userRepository, internshipRepository, applicationRepository,
                registrationRequestRepository, withdrawalRequestRepository, internshipService, currentUser);
        this.student = (Student) currentUser;
    }

    @Override
    public void display() {
        boolean running = true;

        while (running) {
            printStudentMenu();
            int choice = CLIUtil.readInt("Enter your choice: ", 0, 6);

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
                "Change Password"
        };
        printMenuOptions(options);
    }

    private void viewAvailableInternships() {
        CLIUtil.printHeader("Available Internships");

        List<InternshipOpportunity> availableOpportunities = internshipService.listAvailableInternships();

        if (availableOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No internships available at the moment.");
        } else {
            displayOpportunityList(availableOpportunities);
        }

        CLIUtil.pause();
    }

    private void applyForInternship() {
        CLIUtil.printHeader("Apply for Internship");

        List<InternshipOpportunity> availableOpportunities = internshipService.listAvailableInternships();

        if (availableOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No internships available to apply for.");
            CLIUtil.pause();
            return;
        }

        displayOpportunityList(availableOpportunities);

        String oppId = CLIUtil.readString("\nEnter Internship ID to apply (or 'cancel'): ");

        if (oppId.equalsIgnoreCase("cancel")) {
            return;
        }

        InternshipOpportunity opportunity = internshipRepository.findById(oppId).orElse(null);

        if (opportunity == null) {
            CLIUtil.displayError("Internship not found.");
            CLIUtil.pause();
            return;
        }

        // Check if already applied
        boolean alreadyApplied = applicationRepository.findAll().stream()
                .anyMatch(app -> app.getStudent().getId().equals(student.getId())
                        && app.getOpportunity().getId().equals(oppId));

        if (alreadyApplied) {
            CLIUtil.displayError("You have already applied for this internship.");
            CLIUtil.pause();
            return;
        }

        // Create application
        String appId = "APP" + System.currentTimeMillis();
        InternshipApplication application = new InternshipApplication(appId, student, opportunity);
        applicationRepository.save(application);

        CLIUtil.displaySuccess("Application submitted successfully! Application ID: " + appId);
        CLIUtil.pause();
    }

    private void viewMyApplications() {
        CLIUtil.printHeader("My Applications");

        List<InternshipApplication> myApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getStudent().getId().equals(student.getId()))
                .collect(Collectors.toList());

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

        List<InternshipApplication> pendingApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getStudent().getId().equals(student.getId()))
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingApplications.isEmpty()) {
            CLIUtil.displayInfo("No pending applications to withdraw.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Your pending applications:");
        for (int i = 0; i < pendingApplications.size(); i++) {
            InternshipApplication app = pendingApplications.get(i);
            System.out.println((i + 1) + ". " + app.getId() + " - " +
                    app.getOpportunity().getTitle() + " at " +
                    app.getOpportunity().getCompanyName());
        }

        int choice = CLIUtil.readInt("\nSelect application to withdraw (0 to cancel): ",
                0, pendingApplications.size());

        if (choice == 0) {
            return;
        }

        InternshipApplication selectedApp = pendingApplications.get(choice - 1);
        String reason = CLIUtil.readString("Enter reason for withdrawal: ");

        String requestId = "WR" + System.currentTimeMillis();
        WithdrawalRequest request = new WithdrawalRequest(requestId, selectedApp, reason);
        withdrawalRequestRepository.save(request);

        selectedApp.setStatus(ApplicationStatus.WITHDRAWAL_REQUESTED);
        applicationRepository.save(selectedApp);

        CLIUtil.displaySuccess("Withdrawal request submitted. Request ID: " + requestId);
        CLIUtil.pause();
    }

    private void acceptOrDeclinePlacement() {
        CLIUtil.printHeader("Accept/Decline Placement");

        List<InternshipApplication> successfulApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getStudent().getId().equals(student.getId()))
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .filter(app -> !app.isPlacementAccepted())
                .collect(Collectors.toList());

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
            selectedApp.setPlacementAccepted(true);
            applicationRepository.save(selectedApp);
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
