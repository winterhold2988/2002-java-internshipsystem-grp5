package code.data;

import code.enums.ApplicationStatus;
import code.enums.OpportunityStatus;
import code.model.*;
import code.repository.*;

import java.util.HashMap;
import java.util.Map;

/**
 * DataStatistics provides methods to calculate and display statistics about the data
 * in the system. This is useful for administrative reporting and system monitoring.
 */
public class DataStatistics {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;

    public DataStatistics(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.withdrawalRequestRepository = withdrawalRequestRepository;
    }

    /**
     * Counts the total number of students in the system.
     *
     * @return the number of students
     */
    public long countStudents() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Student)
                .count();
    }

    /**
     * Counts the total number of career center staff in the system.
     *
     * @return the number of staff members
     */
    public long countStaff() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof CareerCenterStaff)
                .count();
    }

    /**
     * Counts the total number of company representatives in the system.
     *
     * @return the number of company representatives
     */
    public long countCompanyRepresentatives() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof CompanyRepresentative)
                .count();
    }

    /**
     * Counts the number of approved company representatives.
     *
     * @return the number of approved representatives
     */
    public long countApprovedRepresentatives() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof CompanyRepresentative)
                .map(user -> (CompanyRepresentative) user)
                .filter(CompanyRepresentative::isApproved)
                .count();
    }

    /**
     * Counts the total number of internship opportunities in the system.
     *
     * @return the number of opportunities
     */
    public long countOpportunities() {
        return internshipRepository.findAll().size();
    }

    /**
     * Counts internship opportunities by their status.
     *
     * @return a map of opportunity status to count
     */
    public Map<OpportunityStatus, Long> countOpportunitiesByStatus() {
        Map<OpportunityStatus, Long> stats = new HashMap<>();
        
        for (OpportunityStatus status : OpportunityStatus.values()) {
            long count = internshipRepository.findAll().stream()
                    .filter(opp -> opp.getStatus() == status)
                    .count();
            stats.put(status, count);
        }
        
        return stats;
    }

    /**
     * Counts the total number of internship applications in the system.
     *
     * @return the number of applications
     */
    public long countApplications() {
        return applicationRepository.findAll().size();
    }

    /**
     * Counts internship applications by their status.
     *
     * @return a map of application status to count
     */
    public Map<ApplicationStatus, Long> countApplicationsByStatus() {
        Map<ApplicationStatus, Long> stats = new HashMap<>();
        
        for (ApplicationStatus status : ApplicationStatus.values()) {
            long count = applicationRepository.findAll().stream()
                    .filter(app -> app.getStatus() == status)
                    .count();
            stats.put(status, count);
        }
        
        return stats;
    }

    /**
     * Counts the number of pending registration requests.
     *
     * @return the number of pending requests
     */
    public long countPendingRegistrationRequests() {
        return registrationRequestRepository.findAll().size();
    }

    /**
     * Counts the number of pending withdrawal requests.
     *
     * @return the number of pending withdrawal requests
     */
    public long countPendingWithdrawalRequests() {
        return withdrawalRequestRepository.findAll().size();
    }

    /**
     * Generates a comprehensive statistics report.
     *
     * @return a formatted string containing all statistics
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== System Data Statistics ===\n\n");
        
        // User statistics
        report.append("Users:\n");
        report.append(String.format("  Students: %d\n", countStudents()));
        report.append(String.format("  Career Center Staff: %d\n", countStaff()));
        report.append(String.format("  Company Representatives: %d (Approved: %d)\n\n", 
                countCompanyRepresentatives(), countApprovedRepresentatives()));
        
        // Opportunity statistics
        report.append("Internship Opportunities:\n");
        report.append(String.format("  Total: %d\n", countOpportunities()));
        Map<OpportunityStatus, Long> oppStats = countOpportunitiesByStatus();
        for (Map.Entry<OpportunityStatus, Long> entry : oppStats.entrySet()) {
            report.append(String.format("    %s: %d\n", entry.getKey(), entry.getValue()));
        }
        report.append("\n");
        
        // Application statistics
        report.append("Internship Applications:\n");
        report.append(String.format("  Total: %d\n", countApplications()));
        Map<ApplicationStatus, Long> appStats = countApplicationsByStatus();
        for (Map.Entry<ApplicationStatus, Long> entry : appStats.entrySet()) {
            report.append(String.format("    %s: %d\n", entry.getKey(), entry.getValue()));
        }
        report.append("\n");
        
        // Request statistics
        report.append("Pending Requests:\n");
        report.append(String.format("  Registration Requests: %d\n", countPendingRegistrationRequests()));
        report.append(String.format("  Withdrawal Requests: %d\n", countPendingWithdrawalRequests()));
        
        return report.toString();
    }

    /**
     * Prints the statistics report to the console.
     */
    public void printReport() {
        System.out.println(generateReport());
    }
}
