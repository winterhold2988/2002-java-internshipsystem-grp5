package code.dto;

import code.enums.OpportunityStatus;
import code.model.InternshipOpportunity;

import java.time.LocalDate;

/**
 * Data transfer object representing summary information about an internship opportunity.
 * Used for reports and display purposes.
 */
public class OpportunityReportDTO {

    private final String id;
    private final String title;
    private final String companyName;
    private final String level;
    private final String preferredMajor;
    private final LocalDate openingDate;
    private final LocalDate closingDate;
    private final OpportunityStatus status;
    private final int maxSlots;
    private final int confirmedSlots;
    private final int availableSlots;
    private final boolean visible;
    private final boolean available;
    private final String createdBy;

    private OpportunityReportDTO(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.companyName = builder.companyName;
        this.level = builder.level;
        this.preferredMajor = builder.preferredMajor;
        this.openingDate = builder.openingDate;
        this.closingDate = builder.closingDate;
        this.status = builder.status;
        this.maxSlots = builder.maxSlots;
        this.confirmedSlots = builder.confirmedSlots;
        this.availableSlots = builder.availableSlots;
        this.visible = builder.visible;
        this.available = builder.available;
        this.createdBy = builder.createdBy;
    }

    /**
     * Creates a report DTO from an InternshipOpportunity entity.
     *
     * @param opportunity the opportunity to convert
     * @return a new OpportunityReportDTO
     */
    public static OpportunityReportDTO from(InternshipOpportunity opportunity) {
        return builder()
                .id(opportunity.getId())
                .title(opportunity.getTitle())
                .companyName(opportunity.getCompanyName())
                .level(opportunity.getLevel() != null ? opportunity.getLevel().toString() : "N/A")
                .preferredMajor(opportunity.getPreferredMajor())
                .openingDate(opportunity.getOpeningDate())
                .closingDate(opportunity.getClosingDate())
                .status(opportunity.getStatus())
                .maxSlots(opportunity.getMaxSlots())
                .confirmedSlots(opportunity.getConfirmedSlots())
                .availableSlots(opportunity.getMaxSlots() - opportunity.getConfirmedSlots())
                .visible(opportunity.isVisible())
                .available(opportunity.isAvailable())
                .createdBy(opportunity.getCreatedBy() != null ? opportunity.getCreatedBy().getName() : "Unknown")
                .build();
    }

    /**
     * Formats this DTO as a readable string for display.
     *
     * @return a formatted string representation
     */
    public String toDisplayString() {
        return String.format(
                "ID: %s | Title: %s | Company: %s | Level: %s | Major: %s\n" +
                "Opening: %s | Closing: %s | Status: %s | Visible: %s\n" +
                "Slots: %d/%d (Available: %d) | Created By: %s",
                id, title, companyName, level, preferredMajor,
                openingDate, closingDate, status, visible ? "Yes" : "No",
                confirmedSlots, maxSlots, availableSlots, createdBy
        );
    }

    /**
     * Formats this DTO as a single line summary.
     *
     * @return a one-line summary
     */
    public String toSummaryString() {
        return String.format("[%s] %s - %s (%s) | Slots: %d/%d | Status: %s",
                id, title, companyName, level, confirmedSlots, maxSlots, status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String title;
        private String companyName;
        private String level;
        private String preferredMajor;
        private LocalDate openingDate;
        private LocalDate closingDate;
        private OpportunityStatus status;
        private int maxSlots;
        private int confirmedSlots;
        private int availableSlots;
        private boolean visible;
        private boolean available;
        private String createdBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder level(String level) { this.level = level; return this; }
        public Builder preferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; return this; }
        public Builder openingDate(LocalDate openingDate) { this.openingDate = openingDate; return this; }
        public Builder closingDate(LocalDate closingDate) { this.closingDate = closingDate; return this; }
        public Builder status(OpportunityStatus status) { this.status = status; return this; }
        public Builder maxSlots(int maxSlots) { this.maxSlots = maxSlots; return this; }
        public Builder confirmedSlots(int confirmedSlots) { this.confirmedSlots = confirmedSlots; return this; }
        public Builder availableSlots(int availableSlots) { this.availableSlots = availableSlots; return this; }
        public Builder visible(boolean visible) { this.visible = visible; return this; }
        public Builder available(boolean available) { this.available = available; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }

        public OpportunityReportDTO build() {
            return new OpportunityReportDTO(this);
        }
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCompanyName() { return companyName; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public OpportunityStatus getStatus() { return status; }
    public int getMaxSlots() { return maxSlots; }
    public int getConfirmedSlots() { return confirmedSlots; }
    public int getAvailableSlots() { return availableSlots; }
    public boolean isVisible() { return visible; }
    public boolean isAvailable() { return available; }
    public String getCreatedBy() { return createdBy; }
}
