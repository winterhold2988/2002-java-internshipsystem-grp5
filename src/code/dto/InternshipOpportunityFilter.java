package code.dto;

import code.enums.InternshipLevel;
import code.enums.OpportunityStatus;
import code.model.InternshipOpportunity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Filter criteria for searching and filtering internship opportunities.
 * Uses the builder pattern for flexible filter construction.
 */
public class InternshipOpportunityFilter {

    private String companyName;
    private OpportunityStatus status;
    private InternshipLevel level;
    private String preferredMajor;
    private Boolean visible;
    private Boolean availableOnly;
    private LocalDate openingDateFrom;
    private LocalDate openingDateTo;
    private LocalDate closingDateFrom;
    private LocalDate closingDateTo;
    private Integer minSlots;
    private String createdById;
    private String titleContains;

    private InternshipOpportunityFilter() {
        // Private constructor - use builder
    }

    /**
     * Applies this filter to a collection of opportunities.
     *
     * @param opportunities the opportunities to filter
     * @return a stream of filtered opportunities
     */
    public Stream<InternshipOpportunity> apply(Collection<InternshipOpportunity> opportunities) {
        Stream<InternshipOpportunity> stream = opportunities.stream();

        if (companyName != null) {
            stream = stream.filter(opp -> opp.getCompanyName() != null && 
                    opp.getCompanyName().equalsIgnoreCase(companyName));
        }

        if (status != null) {
            stream = stream.filter(opp -> opp.getStatus() == status);
        }

        if (level != null) {
            stream = stream.filter(opp -> opp.getLevel() == level);
        }

        if (preferredMajor != null) {
            stream = stream.filter(opp -> opp.getPreferredMajor() != null && 
                    opp.getPreferredMajor().equalsIgnoreCase(preferredMajor));
        }

        if (visible != null) {
            stream = stream.filter(opp -> opp.isVisible() == visible);
        }

        if (availableOnly != null && availableOnly) {
            stream = stream.filter(InternshipOpportunity::isAvailable);
        }

        if (openingDateFrom != null) {
            stream = stream.filter(opp -> opp.getOpeningDate() != null && 
                    !opp.getOpeningDate().isBefore(openingDateFrom));
        }

        if (openingDateTo != null) {
            stream = stream.filter(opp -> opp.getOpeningDate() != null && 
                    !opp.getOpeningDate().isAfter(openingDateTo));
        }

        if (closingDateFrom != null) {
            stream = stream.filter(opp -> opp.getClosingDate() != null && 
                    !opp.getClosingDate().isBefore(closingDateFrom));
        }

        if (closingDateTo != null) {
            stream = stream.filter(opp -> opp.getClosingDate() != null && 
                    !opp.getClosingDate().isAfter(closingDateTo));
        }

        if (minSlots != null) {
            stream = stream.filter(opp -> (opp.getMaxSlots() - opp.getConfirmedSlots()) >= minSlots);
        }

        if (createdById != null) {
            stream = stream.filter(opp -> opp.getCreatedBy() != null && 
                    opp.getCreatedBy().getId().equals(createdById));
        }

        if (titleContains != null) {
            String lowerSearch = titleContains.toLowerCase();
            stream = stream.filter(opp -> opp.getTitle() != null && 
                    opp.getTitle().toLowerCase().contains(lowerSearch));
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
     * Builder class for InternshipOpportunityFilter.
     */
    public static class Builder {
        private final InternshipOpportunityFilter filter;

        private Builder() {
            this.filter = new InternshipOpportunityFilter();
        }

        public Builder companyName(String companyName) {
            filter.companyName = companyName;
            return this;
        }

        public Builder status(OpportunityStatus status) {
            filter.status = status;
            return this;
        }

        public Builder level(InternshipLevel level) {
            filter.level = level;
            return this;
        }

        public Builder preferredMajor(String preferredMajor) {
            filter.preferredMajor = preferredMajor;
            return this;
        }

        public Builder visible(Boolean visible) {
            filter.visible = visible;
            return this;
        }

        public Builder availableOnly(Boolean availableOnly) {
            filter.availableOnly = availableOnly;
            return this;
        }

        public Builder openingDateFrom(LocalDate openingDateFrom) {
            filter.openingDateFrom = openingDateFrom;
            return this;
        }

        public Builder openingDateTo(LocalDate openingDateTo) {
            filter.openingDateTo = openingDateTo;
            return this;
        }

        public Builder closingDateFrom(LocalDate closingDateFrom) {
            filter.closingDateFrom = closingDateFrom;
            return this;
        }

        public Builder closingDateTo(LocalDate closingDateTo) {
            filter.closingDateTo = closingDateTo;
            return this;
        }

        public Builder minSlots(Integer minSlots) {
            filter.minSlots = minSlots;
            return this;
        }

        public Builder createdBy(String userId) {
            filter.createdById = userId;
            return this;
        }

        public Builder titleContains(String text) {
            filter.titleContains = text;
            return this;
        }

        public InternshipOpportunityFilter build() {
            return filter;
        }
    }

    // Getters for all fields
    public String getCompanyName() { return companyName; }
    public OpportunityStatus getStatus() { return status; }
    public InternshipLevel getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public Boolean getVisible() { return visible; }
    public Boolean getAvailableOnly() { return availableOnly; }
    public LocalDate getOpeningDateFrom() { return openingDateFrom; }
    public LocalDate getOpeningDateTo() { return openingDateTo; }
    public LocalDate getClosingDateFrom() { return closingDateFrom; }
    public LocalDate getClosingDateTo() { return closingDateTo; }
    public Integer getMinSlots() { return minSlots; }
    public String getCreatedById() { return createdById; }
    public String getTitleContains() { return titleContains; }
}
