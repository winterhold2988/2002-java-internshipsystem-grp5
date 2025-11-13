package code.dto;

import code.enums.ApplicationStatus;
import code.model.InternshipApplication;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Filter criteria for searching and filtering internship applications.
 * Uses the builder pattern for flexible filter construction.
 */
public class InternshipApplicationFilter {

    private String studentId;
    private String opportunityId;
    private ApplicationStatus status;
    private Boolean placementAccepted;
    private LocalDateTime submittedFrom;
    private LocalDateTime submittedTo;
    private String companyName;
    private String studentMajor;

    private InternshipApplicationFilter() {
        // Private constructor - use builder
    }

    /**
     * Applies this filter to a collection of applications.
     *
     * @param applications the applications to filter
     * @return a stream of filtered applications
     */
    public Stream<InternshipApplication> apply(Collection<InternshipApplication> applications) {
        Stream<InternshipApplication> stream = applications.stream();

        if (studentId != null) {
            stream = stream.filter(app -> app.getStudent().getId().equals(studentId));
        }

        if (opportunityId != null) {
            stream = stream.filter(app -> app.getOpportunity().getId().equals(opportunityId));
        }

        if (status != null) {
            stream = stream.filter(app -> app.getStatus() == status);
        }

        if (placementAccepted != null) {
            stream = stream.filter(app -> app.isPlacementAccepted() == placementAccepted);
        }

        if (submittedFrom != null) {
            stream = stream.filter(app -> !app.getSubmittedAt().isBefore(submittedFrom));
        }

        if (submittedTo != null) {
            stream = stream.filter(app -> !app.getSubmittedAt().isAfter(submittedTo));
        }

        if (companyName != null) {
            stream = stream.filter(app -> app.getOpportunity().getCompanyName() != null && 
                    app.getOpportunity().getCompanyName().equalsIgnoreCase(companyName));
        }

        if (studentMajor != null) {
            stream = stream.filter(app -> app.getStudent().getMajor() != null && 
                    app.getStudent().getMajor().equalsIgnoreCase(studentMajor));
        }

        return stream;
    }

    /**
     * Creates a new builder for constructing a filter.
     *
     * @return a new filter builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for InternshipApplicationFilter.
     */
    public static class Builder {
        private final InternshipApplicationFilter filter;

        private Builder() {
            this.filter = new InternshipApplicationFilter();
        }

        public Builder studentId(String studentId) {
            filter.studentId = studentId;
            return this;
        }

        public Builder opportunityId(String opportunityId) {
            filter.opportunityId = opportunityId;
            return this;
        }

        public Builder status(ApplicationStatus status) {
            filter.status = status;
            return this;
        }

        public Builder placementAccepted(Boolean placementAccepted) {
            filter.placementAccepted = placementAccepted;
            return this;
        }

        public Builder submittedFrom(LocalDateTime submittedFrom) {
            filter.submittedFrom = submittedFrom;
            return this;
        }

        public Builder submittedTo(LocalDateTime submittedTo) {
            filter.submittedTo = submittedTo;
            return this;
        }

        public Builder companyName(String companyName) {
            filter.companyName = companyName;
            return this;
        }

        public Builder studentMajor(String studentMajor) {
            filter.studentMajor = studentMajor;
            return this;
        }

        public InternshipApplicationFilter build() {
            return filter;
        }
    }

    // Getters for all fields
    public String getStudentId() { return studentId; }
    public String getOpportunityId() { return opportunityId; }
    public ApplicationStatus getStatus() { return status; }
    public Boolean getPlacementAccepted() { return placementAccepted; }
    public LocalDateTime getSubmittedFrom() { return submittedFrom; }
    public LocalDateTime getSubmittedTo() { return submittedTo; }
    public String getCompanyName() { return companyName; }
    public String getStudentMajor() { return studentMajor; }
}
