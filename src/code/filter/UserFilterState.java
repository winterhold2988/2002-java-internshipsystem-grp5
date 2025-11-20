package code.filter;

// Stores filter state per user, enabling persistent filter preferences across menu navigation.
// SOLID: SRP (manages single user's filter state), Encapsulation (private mutable state).

public class UserFilterState {
    private final String userId;
    private OpportunityFilterCriteria filterCriteria;

    public UserFilterState(String userId) {
        this.userId = userId;
        // Default: alphabetical sorting with no filters
        this.filterCriteria = OpportunityFilterCriteria.builder()
                .sortAlphabetically(true)
                .build();
    }

    public String getUserId() {
        return userId;
    }

    public OpportunityFilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(OpportunityFilterCriteria filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public void clearFilters() {
        this.filterCriteria = OpportunityFilterCriteria.builder()
                .sortAlphabetically(true)
                .build();
    }

    public boolean hasActiveFilters() {
        return filterCriteria != null && filterCriteria.hasActiveFilters();
    }
}
