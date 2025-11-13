package code.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregated report for a specific company showing all their opportunities and applications.
 */
public class CompanyActivityReport {

    private final String companyName;
    private final String representativeName;
    private final List<OpportunityReportDTO> opportunities;
    private final List<ApplicationReportDTO> applications;
    private final int totalSlots;
    private final int filledSlots;
    private final int pendingApplications;
    private final int successfulApplications;

    private CompanyActivityReport(Builder builder) {
        this.companyName = builder.companyName;
        this.representativeName = builder.representativeName;
        this.opportunities = builder.opportunities;
        this.applications = builder.applications;
        this.totalSlots = builder.totalSlots;
        this.filledSlots = builder.filledSlots;
        this.pendingApplications = builder.pendingApplications;
        this.successfulApplications = builder.successfulApplications;
    }

    /**
     * Generates a formatted report string.
     *
     * @return a formatted company activity report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append(String.format("     COMPANY ACTIVITY REPORT: %s\n", companyName));
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append(String.format("Representative: %s\n\n", representativeName));

        // Summary
        report.append("┌─── SUMMARY ────────────────────────────────────────────┐\n");
        report.append(String.format("│ Total Opportunities Posted:  %8d                 │\n", opportunities.size()));
        report.append(String.format("│ Total Applications Received: %8d                 │\n", applications.size()));
        report.append(String.format("│ Total Slots Available:       %8d                 │\n", totalSlots));
        report.append(String.format("│ Filled Slots:                %8d                 │\n", filledSlots));
        report.append(String.format("│ Pending Applications:        %8d                 │\n", pendingApplications));
        report.append(String.format("│ Successful Placements:       %8d                 │\n", successfulApplications));
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Opportunities
        report.append("┌─── POSTED OPPORTUNITIES ───────────────────────────────┐\n");
        if (opportunities.isEmpty()) {
            report.append("│ No opportunities posted yet.                           │\n");
        } else {
            for (int i = 0; i < opportunities.size(); i++) {
                OpportunityReportDTO opp = opportunities.get(i);
                report.append(String.format("│ %d. %-52s │\n", i + 1, opp.getTitle()));
                report.append(String.format("│    Status: %-12s | Slots: %d/%d                 │\n", 
                        opp.getStatus(), opp.getConfirmedSlots(), opp.getMaxSlots()));
            }
        }
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Recent Applications
        report.append("┌─── RECENT APPLICATIONS ────────────────────────────────┐\n");
        if (applications.isEmpty()) {
            report.append("│ No applications received yet.                          │\n");
        } else {
            int displayCount = Math.min(10, applications.size());
            for (int i = 0; i < displayCount; i++) {
                ApplicationReportDTO app = applications.get(i);
                report.append(String.format("│ %d. %-52s │\n", i + 1, app.getStudentName()));
                report.append(String.format("│    %s - %s                                   │\n", 
                        app.getOpportunityTitle(), app.getStatus()));
            }
            if (applications.size() > 10) {
                report.append(String.format("│ ... and %d more applications                          │\n", 
                        applications.size() - 10));
            }
        }
        report.append("└────────────────────────────────────────────────────────┘\n");
        report.append("═══════════════════════════════════════════════════════════\n");

        return report.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String companyName;
        private String representativeName;
        private List<OpportunityReportDTO> opportunities = new ArrayList<>();
        private List<ApplicationReportDTO> applications = new ArrayList<>();
        private int totalSlots;
        private int filledSlots;
        private int pendingApplications;
        private int successfulApplications;

        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder representativeName(String representativeName) { 
            this.representativeName = representativeName; return this; 
        }
        public Builder opportunities(List<OpportunityReportDTO> opportunities) { 
            this.opportunities = opportunities; return this; 
        }
        public Builder applications(List<ApplicationReportDTO> applications) { 
            this.applications = applications; return this; 
        }
        public Builder totalSlots(int totalSlots) { this.totalSlots = totalSlots; return this; }
        public Builder filledSlots(int filledSlots) { this.filledSlots = filledSlots; return this; }
        public Builder pendingApplications(int pendingApplications) { 
            this.pendingApplications = pendingApplications; return this; 
        }
        public Builder successfulApplications(int successfulApplications) { 
            this.successfulApplications = successfulApplications; return this; 
        }

        public CompanyActivityReport build() {
            return new CompanyActivityReport(this);
        }
    }

    // Getters
    public String getCompanyName() { return companyName; }
    public String getRepresentativeName() { return representativeName; }
    public List<OpportunityReportDTO> getOpportunities() { return opportunities; }
    public List<ApplicationReportDTO> getApplications() { return applications; }
    public int getTotalSlots() { return totalSlots; }
    public int getFilledSlots() { return filledSlots; }
    public int getPendingApplications() { return pendingApplications; }
    public int getSuccessfulApplications() { return successfulApplications; }
}
