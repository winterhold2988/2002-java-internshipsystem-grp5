package code.dto;

import code.enums.ApplicationStatus;
import code.enums.OpportunityStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Comprehensive system statistics report.
 * Aggregates data across all entities for administrative reporting.
 */
public class SystemStatisticsReport {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LocalDateTime generatedAt;
    private final long totalStudents;
    private final long totalStaff;
    private final long totalCompanyReps;
    private final long approvedCompanyReps;
    private final long totalOpportunities;
    private final Map<OpportunityStatus, Long> opportunitiesByStatus;
    private final long totalApplications;
    private final Map<ApplicationStatus, Long> applicationsByStatus;
    private final long pendingRegistrationRequests;
    private final long pendingWithdrawalRequests;
    private final long totalAvailableSlots;
    private final long totalFilledSlots;

    private SystemStatisticsReport(Builder builder) {
        this.generatedAt = builder.generatedAt;
        this.totalStudents = builder.totalStudents;
        this.totalStaff = builder.totalStaff;
        this.totalCompanyReps = builder.totalCompanyReps;
        this.approvedCompanyReps = builder.approvedCompanyReps;
        this.totalOpportunities = builder.totalOpportunities;
        this.opportunitiesByStatus = builder.opportunitiesByStatus;
        this.totalApplications = builder.totalApplications;
        this.applicationsByStatus = builder.applicationsByStatus;
        this.pendingRegistrationRequests = builder.pendingRegistrationRequests;
        this.pendingWithdrawalRequests = builder.pendingWithdrawalRequests;
        this.totalAvailableSlots = builder.totalAvailableSlots;
        this.totalFilledSlots = builder.totalFilledSlots;
    }

    /**
     * Generates a formatted report string.
     *
     * @return a comprehensive formatted report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append("        INTERNSHIP SYSTEM STATISTICS REPORT\n");
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append(String.format("Generated: %s\n\n", generatedAt.format(FORMATTER)));

        // User Statistics
        report.append("┌─── USER STATISTICS ────────────────────────────────────┐\n");
        report.append(String.format("│ Students:                 %8d                     │\n", totalStudents));
        report.append(String.format("│ Career Center Staff:      %8d                     │\n", totalStaff));
        report.append(String.format("│ Company Representatives:  %8d                     │\n", totalCompanyReps));
        report.append(String.format("│   - Approved:             %8d                     │\n", approvedCompanyReps));
        report.append(String.format("│   - Pending Approval:     %8d                     │\n", 
                totalCompanyReps - approvedCompanyReps));
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Opportunity Statistics
        report.append("┌─── INTERNSHIP OPPORTUNITIES ───────────────────────────┐\n");
        report.append(String.format("│ Total Opportunities:      %8d                     │\n", totalOpportunities));
        for (Map.Entry<OpportunityStatus, Long> entry : opportunitiesByStatus.entrySet()) {
            report.append(String.format("│   - %-20s %8d                     │\n", 
                    entry.getKey() + ":", entry.getValue()));
        }
        report.append(String.format("│ Total Available Slots:    %8d                     │\n", totalAvailableSlots));
        report.append(String.format("│ Total Filled Slots:       %8d                     │\n", totalFilledSlots));
        report.append(String.format("│ Slot Fill Rate:           %8.1f%%                    │\n", 
                totalAvailableSlots > 0 ? (totalFilledSlots * 100.0 / totalAvailableSlots) : 0.0));
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Application Statistics
        report.append("┌─── APPLICATION STATISTICS ─────────────────────────────┐\n");
        report.append(String.format("│ Total Applications:       %8d                     │\n", totalApplications));
        for (Map.Entry<ApplicationStatus, Long> entry : applicationsByStatus.entrySet()) {
            report.append(String.format("│   - %-20s %8d                     │\n", 
                    entry.getKey() + ":", entry.getValue()));
        }
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Pending Actions
        report.append("┌─── PENDING ACTIONS ────────────────────────────────────┐\n");
        report.append(String.format("│ Registration Requests:    %8d                     │\n", 
                pendingRegistrationRequests));
        report.append(String.format("│ Withdrawal Requests:      %8d                     │\n", 
                pendingWithdrawalRequests));
        report.append("└────────────────────────────────────────────────────────┘\n");

        report.append("═══════════════════════════════════════════════════════════\n");

        return report.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime generatedAt = LocalDateTime.now();
        private long totalStudents;
        private long totalStaff;
        private long totalCompanyReps;
        private long approvedCompanyReps;
        private long totalOpportunities;
        private Map<OpportunityStatus, Long> opportunitiesByStatus = new HashMap<>();
        private long totalApplications;
        private Map<ApplicationStatus, Long> applicationsByStatus = new HashMap<>();
        private long pendingRegistrationRequests;
        private long pendingWithdrawalRequests;
        private long totalAvailableSlots;
        private long totalFilledSlots;

        public Builder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public Builder totalStudents(long totalStudents) { this.totalStudents = totalStudents; return this; }
        public Builder totalStaff(long totalStaff) { this.totalStaff = totalStaff; return this; }
        public Builder totalCompanyReps(long totalCompanyReps) { this.totalCompanyReps = totalCompanyReps; return this; }
        public Builder approvedCompanyReps(long approvedCompanyReps) { this.approvedCompanyReps = approvedCompanyReps; return this; }
        public Builder totalOpportunities(long totalOpportunities) { this.totalOpportunities = totalOpportunities; return this; }
        public Builder opportunitiesByStatus(Map<OpportunityStatus, Long> opportunitiesByStatus) { 
            this.opportunitiesByStatus = opportunitiesByStatus; return this; 
        }
        public Builder totalApplications(long totalApplications) { this.totalApplications = totalApplications; return this; }
        public Builder applicationsByStatus(Map<ApplicationStatus, Long> applicationsByStatus) { 
            this.applicationsByStatus = applicationsByStatus; return this; 
        }
        public Builder pendingRegistrationRequests(long pendingRegistrationRequests) { 
            this.pendingRegistrationRequests = pendingRegistrationRequests; return this; 
        }
        public Builder pendingWithdrawalRequests(long pendingWithdrawalRequests) { 
            this.pendingWithdrawalRequests = pendingWithdrawalRequests; return this; 
        }
        public Builder totalAvailableSlots(long totalAvailableSlots) { 
            this.totalAvailableSlots = totalAvailableSlots; return this; 
        }
        public Builder totalFilledSlots(long totalFilledSlots) { 
            this.totalFilledSlots = totalFilledSlots; return this; 
        }

        public SystemStatisticsReport build() {
            return new SystemStatisticsReport(this);
        }
    }

    // Getters
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public long getTotalStudents() { return totalStudents; }
    public long getTotalStaff() { return totalStaff; }
    public long getTotalCompanyReps() { return totalCompanyReps; }
    public long getApprovedCompanyReps() { return approvedCompanyReps; }
    public long getTotalOpportunities() { return totalOpportunities; }
    public Map<OpportunityStatus, Long> getOpportunitiesByStatus() { return opportunitiesByStatus; }
    public long getTotalApplications() { return totalApplications; }
    public Map<ApplicationStatus, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public long getPendingRegistrationRequests() { return pendingRegistrationRequests; }
    public long getPendingWithdrawalRequests() { return pendingWithdrawalRequests; }
    public long getTotalAvailableSlots() { return totalAvailableSlots; }
    public long getTotalFilledSlots() { return totalFilledSlots; }
}
