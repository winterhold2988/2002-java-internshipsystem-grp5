package code.model;

import code.enums.WithdrawalDecision;

import java.time.LocalDateTime;

// Represents a student's request to withdraw from an internship application.
public class WithdrawalRequest {

    private final String id;
    private final InternshipApplication application;
    private final String reason;
    private final LocalDateTime requestedAt;
    private WithdrawalDecision decision = WithdrawalDecision.PENDING;
    private LocalDateTime decidedAt;

    public WithdrawalRequest(String id, InternshipApplication application, String reason) {
        this.id = id;
        this.application = application;
        this.reason = reason;
        this.requestedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public InternshipApplication getApplication() {
        return application;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public WithdrawalDecision getDecision() {
        return decision;
    }

    public void setDecision(WithdrawalDecision decision) {
        this.decision = decision;
        this.decidedAt = LocalDateTime.now();
    }

    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }
}
