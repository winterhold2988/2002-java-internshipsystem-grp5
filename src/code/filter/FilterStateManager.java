package code.filter;

// Manages filter state persistence for all users during a session (in-memory storage).
// SOLID: SRP (manages filter state lifecycle), DIP (could be extended to persist to database).

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FilterStateManager {
    // Thread-safe map for multi-user scenarios
    private final Map<String, UserFilterState> userFilterStates;

    public FilterStateManager() {
        this.userFilterStates = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves or creates a filter state for the given user.
     */
    public UserFilterState getFilterState(String userId) {
        return userFilterStates.computeIfAbsent(userId, UserFilterState::new);
    }

    /**
     * Updates the filter criteria for a user.
     */
    public void updateFilterCriteria(String userId, OpportunityFilterCriteria criteria) {
        UserFilterState state = getFilterState(userId);
        state.setFilterCriteria(criteria);
    }

    /**
     * Clears all filters for a user (resets to default).
     */
    public void clearFilters(String userId) {
        UserFilterState state = getFilterState(userId);
        state.clearFilters();
    }

    /**
     * Retrieves the current filter criteria for a user.
     */
    public OpportunityFilterCriteria getFilterCriteria(String userId) {
        UserFilterState state = getFilterState(userId);
        return state.getFilterCriteria();
    }

    /**
     * Checks if a user has active filters set.
     */
    public boolean hasActiveFilters(String userId) {
        UserFilterState state = getFilterState(userId);
        return state.hasActiveFilters();
    }

    /**
     * Removes filter state for a user (cleanup on logout).
     */
    public void removeUserState(String userId) {
        userFilterStates.remove(userId);
    }

    /**
     * Clears all filter states (useful for testing or system reset).
     */
    public void clearAllStates() {
        userFilterStates.clear();
    }
}
