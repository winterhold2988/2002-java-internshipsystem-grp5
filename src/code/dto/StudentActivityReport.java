package code.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregated report for a student showing their profile and application history.
 */
public class StudentActivityReport {

    private final String studentId;
    private final String studentName;
    private final String major;
    private final int yearOfStudy;
    private final List<ApplicationReportDTO> applications;
    private final int totalApplications;
    private final int pendingApplications;
    private final int successfulApplications;
    private final int unsuccessfulApplications;
    private final boolean hasAcceptedPlacement;

    private StudentActivityReport(Builder builder) {
        this.studentId = builder.studentId;
        this.studentName = builder.studentName;
        this.major = builder.major;
        this.yearOfStudy = builder.yearOfStudy;
        this.applications = builder.applications;
        this.totalApplications = builder.totalApplications;
        this.pendingApplications = builder.pendingApplications;
        this.successfulApplications = builder.successfulApplications;
        this.unsuccessfulApplications = builder.unsuccessfulApplications;
        this.hasAcceptedPlacement = builder.hasAcceptedPlacement;
    }

    /**
     * Generates a formatted report string.
     *
     * @return a formatted student activity report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append(String.format("     STUDENT ACTIVITY REPORT: %s\n", studentName));
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append(String.format("Student ID: %s\n", studentId));
        report.append(String.format("Major: %s | Year: %d\n\n", major, yearOfStudy));

        // Summary
        report.append("┌─── APPLICATION SUMMARY ────────────────────────────────┐\n");
        report.append(String.format("│ Total Applications:      %8d                     │\n", totalApplications));
        report.append(String.format("│ Pending:                 %8d                     │\n", pendingApplications));
        report.append(String.format("│ Successful:              %8d                     │\n", successfulApplications));
        report.append(String.format("│ Unsuccessful:            %8d                     │\n", unsuccessfulApplications));
        report.append(String.format("│ Placement Accepted:      %8s                     │\n", 
                hasAcceptedPlacement ? "Yes" : "No"));
        report.append("└────────────────────────────────────────────────────────┘\n\n");

        // Application History
        report.append("┌─── APPLICATION HISTORY ────────────────────────────────┐\n");
        if (applications.isEmpty()) {
            report.append("│ No applications submitted yet.                         │\n");
        } else {
            for (int i = 0; i < applications.size(); i++) {
                ApplicationReportDTO app = applications.get(i);
                report.append(String.format("│ %d. %-52s │\n", i + 1, app.getOpportunityTitle()));
                report.append(String.format("│    Company: %-20s | Status: %-12s │\n", 
                        app.getCompanyName(), app.getStatus()));
                report.append(String.format("│    Submitted: %s                            │\n", 
                        app.getSubmittedAt().toLocalDate()));
                if (i < applications.size() - 1) {
                    report.append("│    ────────────────────────────────────────────────   │\n");
                }
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
        private String studentId;
        private String studentName;
        private String major;
        private int yearOfStudy;
        private List<ApplicationReportDTO> applications = new ArrayList<>();
        private int totalApplications;
        private int pendingApplications;
        private int successfulApplications;
        private int unsuccessfulApplications;
        private boolean hasAcceptedPlacement;

        public Builder studentId(String studentId) { this.studentId = studentId; return this; }
        public Builder studentName(String studentName) { this.studentName = studentName; return this; }
        public Builder major(String major) { this.major = major; return this; }
        public Builder yearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; return this; }
        public Builder applications(List<ApplicationReportDTO> applications) { 
            this.applications = applications; return this; 
        }
        public Builder totalApplications(int totalApplications) { 
            this.totalApplications = totalApplications; return this; 
        }
        public Builder pendingApplications(int pendingApplications) { 
            this.pendingApplications = pendingApplications; return this; 
        }
        public Builder successfulApplications(int successfulApplications) { 
            this.successfulApplications = successfulApplications; return this; 
        }
        public Builder unsuccessfulApplications(int unsuccessfulApplications) { 
            this.unsuccessfulApplications = unsuccessfulApplications; return this; 
        }
        public Builder hasAcceptedPlacement(boolean hasAcceptedPlacement) { 
            this.hasAcceptedPlacement = hasAcceptedPlacement; return this; 
        }

        public StudentActivityReport build() {
            return new StudentActivityReport(this);
        }
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getMajor() { return major; }
    public int getYearOfStudy() { return yearOfStudy; }
    public List<ApplicationReportDTO> getApplications() { return applications; }
    public int getTotalApplications() { return totalApplications; }
    public int getPendingApplications() { return pendingApplications; }
    public int getSuccessfulApplications() { return successfulApplications; }
    public int getUnsuccessfulApplications() { return unsuccessfulApplications; }
    public boolean hasAcceptedPlacement() { return hasAcceptedPlacement; }
}
