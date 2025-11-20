package code.cli;

import code.enums.*;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterCriteria;
import code.filter.OpportunityFilterService;
import code.model.*;
import code.repository.*;
import code.service.InternshipService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Menu for Career Center Staff users.
 */
public class CareerCenterStaffMenu extends MenuBase {

    public CareerCenterStaffMenu(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipService internshipService,
            OpportunityFilterService opportunityFilterService,
            FilterStateManager filterStateManager,
            User currentUser) {
        super(userRepository, internshipRepository, applicationRepository,
                registrationRequestRepository, withdrawalRequestRepository, internshipService,
                opportunityFilterService, filterStateManager, currentUser);
    }

    @Override
    public void display() {
        boolean running = true;

        while (running) {
            printStaffMenu();
            int choice = CLIUtil.readInt("Enter your choice: ", 0, 10);

            switch (choice) {
                case 1:
                    reviewRegistrationRequests();
                    break;
                case 2:
                    reviewInternshipOpportunities();
                    break;
                case 3:
                    viewAllOpportunities();
                    break;
                case 4:
                    viewAllApplications();
                    break;
                case 5:
                    reviewWithdrawalRequests();
                    break;
                case 6:
                    generateReports();
                    break;
                case 7:
                    manageUsers();
                    break;
                case 8:
                    configureFilters();
                    break;
                case 9:
                    clearFilters();
                    break;
                case 10:
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

    private void printStaffMenu() {
        printMenuHeader("Career Center Staff Menu");
        String[] options = {
                "Review Registration Requests",
                "Review Internship Opportunities",
                "View All Opportunities",
                "View All Applications",
                "Review Withdrawal Requests",
                "Generate Reports",
                "Manage Users",
                "Configure Filters",
                "Clear Filters",
                "Change Password"
        };
        printMenuOptions(options);
    }

    private void reviewRegistrationRequests() {
        CLIUtil.printHeader("Review Registration Requests");

        List<RegistrationRequest> pendingRequests = registrationRequestRepository.findAll().stream()
                .filter(req -> !req.isReviewed())
                .collect(Collectors.toList());

        if (pendingRequests.isEmpty()) {
            CLIUtil.displayInfo("No pending registration requests.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Pending registration requests:");
        for (int i = 0; i < pendingRequests.size(); i++) {
            RegistrationRequest req = pendingRequests.get(i);
            CompanyRepresentative rep = req.getRepresentative();
            System.out.println((i + 1) + ". " + rep.getName() + " - " +
                    rep.getCompanyName() + " (ID: " + rep.getId() + ")");
        }

        int choice = CLIUtil.readInt("\nSelect request to review (0 to cancel): ",
                0, pendingRequests.size());

        if (choice == 0) {
            return;
        }

        RegistrationRequest selectedRequest = pendingRequests.get(choice - 1);
        CompanyRepresentative rep = selectedRequest.getRepresentative();

        System.out.println("\nRepresentative Details:");
        System.out.println("Name: " + rep.getName());
        System.out.println("ID: " + rep.getId());
        System.out.println("Company: " + rep.getCompanyName());
        System.out.println("Department: " + rep.getDepartment());
        System.out.println("Position: " + rep.getPosition());
        System.out.println("Submitted: " + selectedRequest.getSubmittedAt());

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");

        int decision = CLIUtil.readInt("Enter your decision: ", 0, 2);

        switch (decision) {
            case 1:
                selectedRequest.setApproved(true);
                rep.setApproved(true);
                registrationRequestRepository.save(selectedRequest);
                userRepository.save(rep);
                CLIUtil.displaySuccess("Registration approved!");
                break;
            case 2:
                selectedRequest.setApproved(false);
                rep.setApproved(false);
                registrationRequestRepository.save(selectedRequest);
                userRepository.save(rep);
                CLIUtil.displaySuccess("Registration rejected.");
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void reviewInternshipOpportunities() {
        CLIUtil.printHeader("Review Internship Opportunities");

        List<InternshipOpportunity> pendingOpportunities = internshipRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No pending opportunities to review.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Pending opportunities:");
        for (int i = 0; i < pendingOpportunities.size(); i++) {
            InternshipOpportunity opp = pendingOpportunities.get(i);
            System.out.println((i + 1) + ". " + opp.getTitle() + " - " +
                    opp.getCompanyName() + " (ID: " + opp.getId() + ")");
        }

        int choice = CLIUtil.readInt("\nSelect opportunity to review (0 to cancel): ",
                0, pendingOpportunities.size());

        if (choice == 0) {
            return;
        }

        InternshipOpportunity selectedOpp = pendingOpportunities.get(choice - 1);
        displayOpportunityDetails(selectedOpp);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");

        int decision = CLIUtil.readInt("Enter your decision: ", 0, 2);

        switch (decision) {
            case 1:
                selectedOpp.setStatus(OpportunityStatus.APPROVED);
                selectedOpp.setVisible(true);
                internshipRepository.save(selectedOpp);
                CLIUtil.displaySuccess("Opportunity approved and made visible!");
                break;
            case 2:
                selectedOpp.setStatus(OpportunityStatus.REJECTED);
                internshipRepository.save(selectedOpp);
                CLIUtil.displaySuccess("Opportunity rejected.");
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void viewAllOpportunities() {
        CLIUtil.printHeader("All Internship Opportunities");

        List<InternshipOpportunity> opportunities = internshipRepository.findAll().stream()
                .collect(Collectors.toList());

        // Apply user's filter settings
        OpportunityFilterCriteria filterCriteria = filterStateManager.getFilterCriteria(currentUser.getId());
        List<InternshipOpportunity> filteredOpportunities = opportunityFilterService.filterOpportunities(opportunities, filterCriteria);

        if (filterCriteria.hasActiveFilters()) {
            System.out.println(filterCriteria.toString());
            CLIUtil.printSeparator();
        }

        if (filteredOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No opportunities match your criteria.");
        } else {
            for (InternshipOpportunity opp : filteredOpportunities) {
                displayOpportunityDetails(opp);
            }
        }

        CLIUtil.pause();
    }

    private void viewAllApplications() {
        CLIUtil.printHeader("All Applications");

        List<InternshipApplication> applications = applicationRepository.findAll().stream()
                .collect(Collectors.toList());

        if (applications.isEmpty()) {
            CLIUtil.displayInfo("No applications available.");
        } else {
            for (InternshipApplication app : applications) {
                displayApplicationDetails(app);
            }
        }

        CLIUtil.pause();
    }

    private void reviewWithdrawalRequests() {
        CLIUtil.printHeader("Review Withdrawal Requests");

        List<WithdrawalRequest> pendingRequests = withdrawalRequestRepository.findAll().stream()
                .filter(req -> req.getDecision() == WithdrawalDecision.PENDING)
                .collect(Collectors.toList());

        if (pendingRequests.isEmpty()) {
            CLIUtil.displayInfo("No pending withdrawal requests.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Pending withdrawal requests:");
        for (int i = 0; i < pendingRequests.size(); i++) {
            WithdrawalRequest req = pendingRequests.get(i);
            InternshipApplication app = req.getApplication();
            System.out.println((i + 1) + ". " + app.getStudent().getName() +
                    " - Application ID: " + app.getId());
        }

        int choice = CLIUtil.readInt("\nSelect request to review (0 to cancel): ",
                0, pendingRequests.size());

        if (choice == 0) {
            return;
        }

        WithdrawalRequest selectedRequest = pendingRequests.get(choice - 1);
        InternshipApplication app = selectedRequest.getApplication();

        System.out.println("\nWithdrawal Request Details:");
        System.out.println("Student: " + app.getStudent().getName());
        System.out.println("Application ID: " + app.getId());
        System.out.println("Internship: " + app.getOpportunity().getTitle());
        System.out.println("Company: " + app.getOpportunity().getCompanyName());
        System.out.println("Reason: " + selectedRequest.getReason());
        System.out.println("Requested: " + selectedRequest.getRequestedAt());

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");

        int decision = CLIUtil.readInt("Enter your decision: ", 0, 2);

        switch (decision) {
            case 1:
                selectedRequest.setDecision(WithdrawalDecision.APPROVED);
                app.setStatus(ApplicationStatus.UNSUCCESSFUL);

                // Free up the slot
                InternshipOpportunity opp = app.getOpportunity();
                if (opp.getConfirmedSlots() > 0) {
                    opp.setConfirmedSlots(opp.getConfirmedSlots() - 1);
                    if (opp.getStatus() == OpportunityStatus.FILLED) {
                        opp.setStatus(OpportunityStatus.APPROVED);
                    }
                    internshipRepository.save(opp);
                }

                withdrawalRequestRepository.save(selectedRequest);
                applicationRepository.save(app);
                CLIUtil.displaySuccess("Withdrawal approved!");
                break;
            case 2:
                selectedRequest.setDecision(WithdrawalDecision.REJECTED);
                app.setStatus(ApplicationStatus.PENDING);
                withdrawalRequestRepository.save(selectedRequest);
                applicationRepository.save(app);
                CLIUtil.displaySuccess("Withdrawal rejected.");
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void generateReports() {
        CLIUtil.printHeader("Generate Reports");

        System.out.println("Available Reports:");
        System.out.println("1. Registration Statistics");
        System.out.println("2. Opportunity Statistics");
        System.out.println("3. Application Statistics");
        System.out.println("4. Student Placement Status");
        System.out.println("0. Back");

        int choice = CLIUtil.readInt("\nSelect report: ", 0, 4);

        switch (choice) {
            case 1:
                generateRegistrationStats();
                break;
            case 2:
                generateOpportunityStats();
                break;
            case 3:
                generateApplicationStats();
                break;
            case 4:
                generatePlacementStats();
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void generateRegistrationStats() {
        CLIUtil.printHeader("Registration Statistics");

        long total = registrationRequestRepository.findAll().size();
        long pending = registrationRequestRepository.findAll().stream()
                .filter(req -> !req.isReviewed())
                .count();
        long approved = registrationRequestRepository.findAll().stream()
                .filter(RegistrationRequest::isApproved)
                .count();
        long rejected = registrationRequestRepository.findAll().stream()
                .filter(req -> req.isReviewed() && !req.isApproved())
                .count();

        System.out.println("Total Requests: " + total);
        System.out.println("Pending: " + pending);
        System.out.println("Approved: " + approved);
        System.out.println("Rejected: " + rejected);
    }

    private void generateOpportunityStats() {
        CLIUtil.printHeader("Opportunity Statistics");

        long total = internshipRepository.findAll().size();
        long pending = internshipRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.PENDING)
                .count();
        long approved = internshipRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.APPROVED)
                .count();
        long rejected = internshipRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.REJECTED)
                .count();
        long filled = internshipRepository.findAll().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.FILLED)
                .count();

        System.out.println("Total Opportunities: " + total);
        System.out.println("Pending: " + pending);
        System.out.println("Approved: " + approved);
        System.out.println("Rejected: " + rejected);
        System.out.println("Filled: " + filled);
    }

    private void generateApplicationStats() {
        CLIUtil.printHeader("Application Statistics");

        long total = applicationRepository.findAll().size();
        long pending = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .count();
        long successful = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .count();
        long unsuccessful = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.UNSUCCESSFUL)
                .count();
        long withdrawalRequested = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.WITHDRAWAL_REQUESTED)
                .count();

        System.out.println("Total Applications: " + total);
        System.out.println("Pending: " + pending);
        System.out.println("Successful: " + successful);
        System.out.println("Unsuccessful: " + unsuccessful);
        System.out.println("Withdrawal Requested: " + withdrawalRequested);
    }

    private void generatePlacementStats() {
        CLIUtil.printHeader("Student Placement Status");

        long successfulApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .count();
        long acceptedPlacements = applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .filter(InternshipApplication::isPlacementAccepted)
                .count();

        System.out.println("Successful Applications: " + successfulApplications);
        System.out.println("Accepted Placements: " + acceptedPlacements);
        System.out.println("Awaiting Student Response: " + (successfulApplications - acceptedPlacements));
    }

    private void manageUsers() {
        CLIUtil.printHeader("Manage Users");

        System.out.println("1. View All Students");
        System.out.println("2. View All Company Representatives");
        System.out.println("3. View All Staff");
        System.out.println("0. Back");

        int choice = CLIUtil.readInt("\nSelect option: ", 0, 3);

        switch (choice) {
            case 1:
                viewUsersByRole(UserRole.STUDENT);
                break;
            case 2:
                viewUsersByRole(UserRole.COMPANY_REPRESENTATIVE);
                break;
            case 3:
                viewUsersByRole(UserRole.CAREER_CENTER_STAFF);
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void viewUsersByRole(UserRole role) {
        CLIUtil.printHeader(role + " Users");

        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            CLIUtil.displayInfo("No users found.");
        } else {
            for (User user : users) {
                System.out.println("\nID: " + user.getId());
                System.out.println("Name: " + user.getName());

                if (user instanceof Student) {
                    Student student = (Student) user;
                    System.out.println("Major: " + student.getMajor());
                    System.out.println("Year: " + student.getYearOfStudy());
                } else if (user instanceof CompanyRepresentative) {
                    CompanyRepresentative rep = (CompanyRepresentative) user;
                    System.out.println("Company: " + rep.getCompanyName());
                    System.out.println("Approved: " + (rep.isApproved() ? "Yes" : "No"));
                } else if (user instanceof CareerCenterStaff) {
                    CareerCenterStaff s = (CareerCenterStaff) user;
                    System.out.println("Department: " + s.getDepartment());
                }

                CLIUtil.printSeparator();
            }
        }
    }

    private void displayOpportunityDetails(InternshipOpportunity opp) {
        System.out.println("\nID: " + opp.getId());
        System.out.println("Title: " + opp.getTitle());
        System.out.println("Company: " + opp.getCompanyName());
        System.out.println("Description: " + opp.getDescription());
        System.out.println("Level: " + opp.getLevel());
        System.out.println("Preferred Major: " + opp.getPreferredMajor());
        System.out.println("Status: " + opp.getStatus());
        System.out.println("Visible: " + (opp.isVisible() ? "Yes" : "No"));
        System.out.println("Slots: " + opp.getConfirmedSlots() + "/" + opp.getMaxSlots());
        System.out.println("Opening Date: " + CLIUtil.formatDate(opp.getOpeningDate()));
        System.out.println("Closing Date: " + CLIUtil.formatDate(opp.getClosingDate()));
        System.out.println("Created By: " + opp.getCreatedBy().getName());
        CLIUtil.printSeparator();
    }

    private void displayApplicationDetails(InternshipApplication app) {
        System.out.println("\nApplication ID: " + app.getId());
        System.out.println("Student: " + app.getStudent().getName() + " (" + app.getStudent().getId() + ")");
        System.out.println("Internship: " + app.getOpportunity().getTitle());
        System.out.println("Company: " + app.getOpportunity().getCompanyName());
        System.out.println("Status: " + app.getStatus());
        System.out.println("Submitted: " + app.getSubmittedAt());

        if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
            System.out.println("Placement Accepted: " + (app.isPlacementAccepted() ? "Yes" : "No"));
        }

        CLIUtil.printSeparator();
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
