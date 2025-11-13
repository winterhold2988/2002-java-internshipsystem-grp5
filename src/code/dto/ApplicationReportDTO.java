package code.dto;

import code.enums.ApplicationStatus;
import code.model.InternshipApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data transfer object representing summary information about an internship application.
 * Used for reports and display purposes.
 */
public class ApplicationReportDTO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String applicationId;
    private final String studentId;
    private final String studentName;
    private final String studentMajor;
    private final int studentYear;
    private final String opportunityId;
    private final String opportunityTitle;
    private final String companyName;
    private final ApplicationStatus status;
    private final LocalDateTime submittedAt;
    private final boolean placementAccepted;

    private ApplicationReportDTO(Builder builder) {
        this.applicationId = builder.applicationId;
        this.studentId = builder.studentId;
        this.studentName = builder.studentName;
        this.studentMajor = builder.studentMajor;
        this.studentYear = builder.studentYear;
        this.opportunityId = builder.opportunityId;
        this.opportunityTitle = builder.opportunityTitle;
        this.companyName = builder.companyName;
        this.status = builder.status;
        this.submittedAt = builder.submittedAt;
        this.placementAccepted = builder.placementAccepted;
    }

    /**
     * Creates a report DTO from an InternshipApplication entity.
     *
     * @param application the application to convert
     * @return a new ApplicationReportDTO
     */
    public static ApplicationReportDTO from(InternshipApplication application) {
        return builder()
                .applicationId(application.getId())
                .studentId(application.getStudent().getId())
                .studentName(application.getStudent().getName())
                .studentMajor(application.getStudent().getMajor())
                .studentYear(application.getStudent().getYearOfStudy())
                .opportunityId(application.getOpportunity().getId())
                .opportunityTitle(application.getOpportunity().getTitle())
                .companyName(application.getOpportunity().getCompanyName())
                .status(application.getStatus())
                .submittedAt(application.getSubmittedAt())
                .placementAccepted(application.isPlacementAccepted())
                .build();
    }

    /**
     * Formats this DTO as a readable string for display.
     *
     * @return a formatted string representation
     */
    public String toDisplayString() {
        return String.format(
                "Application ID: %s | Status: %s | Accepted: %s\n" +
                "Student: %s (%s) - %s, Year %d\n" +
                "Opportunity: %s | Title: %s | Company: %s\n" +
                "Submitted: %s",
                applicationId, status, placementAccepted ? "Yes" : "No",
                studentName, studentId, studentMajor, studentYear,
                opportunityId, opportunityTitle, companyName,
                submittedAt.format(FORMATTER)
        );
    }

    /**
     * Formats this DTO as a single line summary.
     *
     * @return a one-line summary
     */
    public String toSummaryString() {
        return String.format("[%s] %s → %s (%s) | Status: %s | Submitted: %s",
                applicationId, studentName, opportunityTitle, companyName, status,
                submittedAt.format(FORMATTER));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String applicationId;
        private String studentId;
        private String studentName;
        private String studentMajor;
        private int studentYear;
        private String opportunityId;
        private String opportunityTitle;
        private String companyName;
        private ApplicationStatus status;
        private LocalDateTime submittedAt;
        private boolean placementAccepted;

        public Builder applicationId(String applicationId) { this.applicationId = applicationId; return this; }
        public Builder studentId(String studentId) { this.studentId = studentId; return this; }
        public Builder studentName(String studentName) { this.studentName = studentName; return this; }
        public Builder studentMajor(String studentMajor) { this.studentMajor = studentMajor; return this; }
        public Builder studentYear(int studentYear) { this.studentYear = studentYear; return this; }
        public Builder opportunityId(String opportunityId) { this.opportunityId = opportunityId; return this; }
        public Builder opportunityTitle(String opportunityTitle) { this.opportunityTitle = opportunityTitle; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder status(ApplicationStatus status) { this.status = status; return this; }
        public Builder submittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; return this; }
        public Builder placementAccepted(boolean placementAccepted) { this.placementAccepted = placementAccepted; return this; }

        public ApplicationReportDTO build() {
            return new ApplicationReportDTO(this);
        }
    }

    // Getters
    public String getApplicationId() { return applicationId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentMajor() { return studentMajor; }
    public int getStudentYear() { return studentYear; }
    public String getOpportunityId() { return opportunityId; }
    public String getOpportunityTitle() { return opportunityTitle; }
    public String getCompanyName() { return companyName; }
    public ApplicationStatus getStatus() { return status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public boolean isPlacementAccepted() { return placementAccepted; }
}
