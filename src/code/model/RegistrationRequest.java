package code.model;

import java.time.LocalDateTime;

// Represents a registration request from a company representative.
public class RegistrationRequest {

    private final String id;
    private final CompanyRepresentative representative;
    private final LocalDateTime submittedAt;
    private boolean approved;
    private boolean reviewed;

    public RegistrationRequest(String id, CompanyRepresentative representative) {
        this.id = id;
        this.representative = representative;
        this.submittedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public CompanyRepresentative getRepresentative() {
        return representative;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
        this.reviewed = true;
    }

    public boolean isReviewed() {
        return reviewed;
    }
}
