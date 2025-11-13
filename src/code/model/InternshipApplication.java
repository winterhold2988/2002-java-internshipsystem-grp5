package code.model;

import code.enums.ApplicationStatus;

import java.time.LocalDateTime;

// Represents a student's application for an internship opportunity.
public class InternshipApplication {

    private final String id;
    private final Student student;
    private final InternshipOpportunity opportunity;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private LocalDateTime submittedAt = LocalDateTime.now();
    private boolean placementAccepted;

    public InternshipApplication(String id, Student student, InternshipOpportunity opportunity) {
        this.id = id;
        this.student = student;
        this.opportunity = opportunity;
    }

    public String getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public boolean isPlacementAccepted() {
        return placementAccepted;
    }

    public void setPlacementAccepted(boolean placementAccepted) {
        this.placementAccepted = placementAccepted;
    }
}
