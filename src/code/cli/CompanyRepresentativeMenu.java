package code.cli;

import code.enums.*;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterCriteria;
import code.filter.OpportunityFilterService;
import code.model.*;
import code.repository.*;
import code.service.InternshipService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Menu for Company Representative users.
 */
public class CompanyRepresentativeMenu extends MenuBase {

    private final CompanyRepresentative representative;

    public CompanyRepresentativeMenu(
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
        this.representative = (CompanyRepresentative) currentUser;
    }

    @Override
    public void display() {
        // Check if representative is approved
        if (!representative.isApproved()) {
            CLIUtil.printHeader("Account Not Approved");
            CLIUtil.displayInfo("Your account is pending approval by Career Center Staff.");
            CLIUtil.displayInfo("You will be able to access the system once approved.");
            CLIUtil.pause();
            return;
        }

        boolean running = true;

        while (running) {
            printCompanyMenu();
            int choice = CLIUtil.readInt("Enter your choice: ", 0, 8);

            switch (choice) {
                case 1:
                    createInternshipOpportunity();
                    break;
                case 2:
                    viewMyOpportunities();
                    break;
                case 3:
                    editInternshipOpportunity();
                    break;
                case 4:
                    viewApplicationsForMyOpportunities();
                    break;
                case 5:
                    reviewApplications();
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

    private void printCompanyMenu() {
        printMenuHeader("Company Representative Menu");
        String[] options = {
                "Create Internship Opportunity",
                "View My Opportunities",
                "Edit Internship Opportunity",
                "View Applications for My Opportunities",
                "Review Applications (Approve/Reject)",
                "Configure Filters",
                "Clear Filters",
                "Change Password"
        };
        printMenuOptions(options);
    }

    private void createInternshipOpportunity() {
        CLIUtil.printHeader("Create Internship Opportunity");

        String id = "OPP" + System.currentTimeMillis();
        InternshipOpportunity opportunity = new InternshipOpportunity(id);

        String title = CLIUtil.readString("Enter internship title: ");
        String description = CLIUtil.readString("Enter description: ");
        String preferredMajor = CLIUtil.readString("Enter preferred major: ");

        System.out.println("\nInternship Level:");
        InternshipLevel[] levels = InternshipLevel.values();
        for (int i = 0; i < levels.length; i++) {
            System.out.println((i + 1) + ". " + levels[i]);
        }
        int levelChoice = CLIUtil.readInt("Select level: ", 1, levels.length);
        InternshipLevel level = levels[levelChoice - 1];

        LocalDate openingDate = CLIUtil.readDate("Enter opening date");
        LocalDate closingDate = CLIUtil.readDate("Enter closing date");

        int maxSlots = CLIUtil.readInt("Enter maximum number of slots: ", 1, 10);

        opportunity.setTitle(title);
        opportunity.setDescription(description);
        opportunity.setLevel(level);
        opportunity.setPreferredMajor(preferredMajor);
        opportunity.setOpeningDate(openingDate);
        opportunity.setClosingDate(closingDate);
        opportunity.setCompanyName(representative.getCompanyName());
        opportunity.setCreatedBy(representative);
        opportunity.setMaxSlots(maxSlots);
        opportunity.setConfirmedSlots(0);
        opportunity.setVisible(false); // Will be visible after staff approval
        opportunity.setStatus(OpportunityStatus.PENDING);

        internshipRepository.save(opportunity);

        CLIUtil.displaySuccess("Internship opportunity created successfully!");
        CLIUtil.displayInfo("Opportunity ID: " + id);
        CLIUtil.displayInfo("Status: Pending approval from Career Center Staff");
        CLIUtil.pause();
    }

    private void viewMyOpportunities() {
        CLIUtil.printHeader("My Internship Opportunities");

        List<InternshipOpportunity> myOpportunities = internshipRepository.findAll().stream()
                .filter(opp -> opp.getCreatedBy().getId().equals(representative.getId()))
                .collect(Collectors.toList());

        // Apply user's filter settings
        OpportunityFilterCriteria filterCriteria = filterStateManager.getFilterCriteria(currentUser.getId());
        List<InternshipOpportunity> filteredOpportunities = opportunityFilterService.filterOpportunities(myOpportunities, filterCriteria);

        if (filterCriteria.hasActiveFilters()) {
            System.out.println(filterCriteria.toString());
            CLIUtil.printSeparator();
        }

        if (filteredOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No opportunities match your criteria.");
        } else {
            displayOpportunityList(filteredOpportunities);
        }

        CLIUtil.pause();
    }

    private void editInternshipOpportunity() {
        CLIUtil.printHeader("Edit Internship Opportunity");

        List<InternshipOpportunity> myOpportunities = internshipRepository.findAll().stream()
                .filter(opp -> opp.getCreatedBy().getId().equals(representative.getId()))
                .filter(opp -> opp.getStatus() == OpportunityStatus.PENDING ||
                        opp.getStatus() == OpportunityStatus.APPROVED)
                .collect(Collectors.toList());

        if (myOpportunities.isEmpty()) {
            CLIUtil.displayInfo("No opportunities available to edit.");
            CLIUtil.pause();
            return;
        }

        displayOpportunityList(myOpportunities);

        String oppId = CLIUtil.readString("\nEnter Opportunity ID to edit (or 'cancel'): ");

        if (oppId.equalsIgnoreCase("cancel")) {
            return;
        }

        InternshipOpportunity opportunity = internshipRepository.findById(oppId).orElse(null);

        if (opportunity == null || !opportunity.getCreatedBy().getId().equals(representative.getId())) {
            CLIUtil.displayError("Opportunity not found or you don't have permission to edit it.");
            CLIUtil.pause();
            return;
        }

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Closing Date");
        System.out.println("4. Maximum Slots");
        System.out.println("5. Visibility (Show/Hide)");
        System.out.println("0. Cancel");

        int choice = CLIUtil.readInt("Enter your choice: ", 0, 5);

        switch (choice) {
            case 1:
                String newTitle = CLIUtil.readString("Enter new title: ");
                opportunity.setTitle(newTitle);
                CLIUtil.displaySuccess("Title updated.");
                break;
            case 2:
                String newDescription = CLIUtil.readString("Enter new description: ");
                opportunity.setDescription(newDescription);
                CLIUtil.displaySuccess("Description updated.");
                break;
            case 3:
                LocalDate newClosingDate = CLIUtil.readDate("Enter new closing date");
                opportunity.setClosingDate(newClosingDate);
                CLIUtil.displaySuccess("Closing date updated.");
                break;
            case 4:
                int newMaxSlots = CLIUtil.readInt("Enter new maximum slots: ", 1, 10);
                if (newMaxSlots < opportunity.getConfirmedSlots()) {
                    CLIUtil.displayError("Cannot set max slots below confirmed slots (" +
                            opportunity.getConfirmedSlots() + ").");
                } else {
                    opportunity.setMaxSlots(newMaxSlots);
                    CLIUtil.displaySuccess("Maximum slots updated.");
                }
                break;
            case 5:
                if (opportunity.getStatus() == OpportunityStatus.APPROVED) {
                    boolean visible = CLIUtil.readYesNo("Make opportunity visible?");
                    opportunity.setVisible(visible);
                    CLIUtil.displaySuccess("Visibility updated.");
                } else {
                    CLIUtil.displayError("Only approved opportunities can be made visible.");
                }
                break;
            case 0:
                return;
        }

        internshipRepository.save(opportunity);
        CLIUtil.pause();
    }

    private void viewApplicationsForMyOpportunities() {
        CLIUtil.printHeader("Applications for My Opportunities");

        List<InternshipApplication> applications = applicationRepository.findAll().stream()
                .filter(app -> app.getOpportunity().getCreatedBy().getId().equals(representative.getId()))
                .collect(Collectors.toList());

        if (applications.isEmpty()) {
            CLIUtil.displayInfo("No applications received yet.");
        } else {
            for (InternshipApplication app : applications) {
                displayApplicationDetails(app);
            }
        }

        CLIUtil.pause();
    }

    private void reviewApplications() {
        CLIUtil.printHeader("Review Applications");

        List<InternshipApplication> pendingApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getOpportunity().getCreatedBy().getId().equals(representative.getId()))
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingApplications.isEmpty()) {
            CLIUtil.displayInfo("No pending applications to review.");
            CLIUtil.pause();
            return;
        }

        System.out.println("Pending applications:");
        for (int i = 0; i < pendingApplications.size(); i++) {
            InternshipApplication app = pendingApplications.get(i);
            System.out.println((i + 1) + ". " + app.getId() + " - " +
                    app.getStudent().getName() + " for " +
                    app.getOpportunity().getTitle());
        }

        int choice = CLIUtil.readInt("\nSelect application to review (0 to cancel): ",
                0, pendingApplications.size());

        if (choice == 0) {
            return;
        }

        InternshipApplication selectedApp = pendingApplications.get(choice - 1);
        displayApplicationDetails(selectedApp);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");

        int decision = CLIUtil.readInt("Enter your decision: ", 0, 2);

        switch (decision) {
            case 1:
                InternshipOpportunity opp = selectedApp.getOpportunity();
                if (opp.getConfirmedSlots() >= opp.getMaxSlots()) {
                    CLIUtil.displayError("All slots for this opportunity are filled.");
                } else {
                    selectedApp.setStatus(ApplicationStatus.SUCCESSFUL);
                    opp.setConfirmedSlots(opp.getConfirmedSlots() + 1);

                    if (opp.getConfirmedSlots() >= opp.getMaxSlots()) {
                        opp.setStatus(OpportunityStatus.FILLED);
                    }

                    applicationRepository.save(selectedApp);
                    internshipRepository.save(opp);
                    CLIUtil.displaySuccess("Application approved!");
                }
                break;
            case 2:
                selectedApp.setStatus(ApplicationStatus.UNSUCCESSFUL);
                applicationRepository.save(selectedApp);
                CLIUtil.displaySuccess("Application rejected.");
                break;
            case 0:
                return;
        }

        CLIUtil.pause();
    }

    private void displayOpportunityList(List<InternshipOpportunity> opportunities) {
        for (InternshipOpportunity opp : opportunities) {
            System.out.println("\nID: " + opp.getId());
            System.out.println("Title: " + opp.getTitle());
            System.out.println("Description: " + opp.getDescription());
            System.out.println("Level: " + opp.getLevel());
            System.out.println("Preferred Major: " + opp.getPreferredMajor());
            System.out.println("Status: " + opp.getStatus());
            System.out.println("Visible: " + (opp.isVisible() ? "Yes" : "No"));
            System.out.println("Slots: " + opp.getConfirmedSlots() + "/" + opp.getMaxSlots());
            System.out.println("Opening Date: " + CLIUtil.formatDate(opp.getOpeningDate()));
            System.out.println("Closing Date: " + CLIUtil.formatDate(opp.getClosingDate()));
            CLIUtil.printSeparator();
        }
    }

    private void displayApplicationDetails(InternshipApplication app) {
        System.out.println("\nApplication ID: " + app.getId());
        System.out.println("Student: " + app.getStudent().getName() + " (" + app.getStudent().getId() + ")");
        System.out.println("Major: " + app.getStudent().getMajor());
        System.out.println("Year: " + app.getStudent().getYearOfStudy());
        System.out.println("Internship: " + app.getOpportunity().getTitle());
        System.out.println("Status: " + app.getStatus());
        System.out.println("Submitted: " + app.getSubmittedAt());
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
