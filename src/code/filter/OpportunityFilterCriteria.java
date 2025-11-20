package code.filter;

// Immutable value object holding filter criteria for internship opportunities.
// SOLID: SRP (single responsibility for criteria), Immutability pattern for thread safety.

import code.enums.InternshipLevel;
import code.enums.OpportunityStatus;
import java.time.LocalDate;

public class OpportunityFilterCriteria {
    private final OpportunityStatus status;
    private final String preferredMajor;
    private final InternshipLevel level;
    private final LocalDate closingDateBefore;
    private final boolean sortAlphabetically;

    private OpportunityFilterCriteria(Builder builder) {
        this.status = builder.status;
        this.preferredMajor = builder.preferredMajor;
        this.level = builder.level;
        this.closingDateBefore = builder.closingDateBefore;
        this.sortAlphabetically = builder.sortAlphabetically;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public LocalDate getClosingDateBefore() {
        return closingDateBefore;
    }

    public boolean isSortAlphabetically() {
        return sortAlphabetically;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpportunityStatus status;
        private String preferredMajor;
        private InternshipLevel level;
        private LocalDate closingDateBefore;
        private boolean sortAlphabetically = true; // Default: alphabetical

        public Builder status(OpportunityStatus status) {
            this.status = status;
            return this;
        }

        public Builder preferredMajor(String preferredMajor) {
            this.preferredMajor = preferredMajor;
            return this;
        }

        public Builder level(InternshipLevel level) {
            this.level = level;
            return this;
        }

        public Builder closingDateBefore(LocalDate closingDateBefore) {
            this.closingDateBefore = closingDateBefore;
            return this;
        }

        public Builder sortAlphabetically(boolean sortAlphabetically) {
            this.sortAlphabetically = sortAlphabetically;
            return this;
        }

        public OpportunityFilterCriteria build() {
            return new OpportunityFilterCriteria(this);
        }
    }

    public boolean hasActiveFilters() {
        return status != null || preferredMajor != null || level != null || closingDateBefore != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Active Filters: ");
        if (status != null) sb.append("Status=").append(status).append(", ");
        if (preferredMajor != null) sb.append("Major=").append(preferredMajor).append(", ");
        if (level != null) sb.append("Level=").append(level).append(", ");
        if (closingDateBefore != null) sb.append("Closing Before=").append(closingDateBefore).append(", ");
        if (sb.toString().endsWith(", ")) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
