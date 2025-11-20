package code.filter;

// Service to filter and sort internship opportunities based on user-defined criteria.
// SOLID: SRP (filtering logic), OCP (extensible via new filter methods), DIP (depends on abstractions).

import code.enums.InternshipLevel;
import code.enums.OpportunityStatus;
import code.model.InternshipOpportunity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpportunityFilterService {

    /**
     * Filters and sorts opportunities based on the provided criteria.
     * Applies filters in sequence, then sorts alphabetically if requested.
     */
    public List<InternshipOpportunity> filterOpportunities(
            List<InternshipOpportunity> opportunities,
            OpportunityFilterCriteria criteria) {

        Stream<InternshipOpportunity> stream = opportunities.stream();

        // Apply status filter
        if (criteria.getStatus() != null) {
            stream = stream.filter(opp -> opp.getStatus() == criteria.getStatus());
        }

        // Apply major filter (case-insensitive)
        if (criteria.getPreferredMajor() != null && !criteria.getPreferredMajor().trim().isEmpty()) {
            String majorFilter = criteria.getPreferredMajor().trim();
            stream = stream.filter(opp -> {
                String oppMajor = opp.getPreferredMajor();
                return oppMajor != null && oppMajor.trim().equalsIgnoreCase(majorFilter);
            });
        }

        // Apply level filter
        if (criteria.getLevel() != null) {
            stream = stream.filter(opp -> opp.getLevel() == criteria.getLevel());
        }

        // Apply closing date filter
        if (criteria.getClosingDateBefore() != null) {
            stream = stream.filter(opp -> 
                opp.getClosingDate() != null && 
                !opp.getClosingDate().isAfter(criteria.getClosingDateBefore())
            );
        }

        List<InternshipOpportunity> filtered = stream.collect(Collectors.toList());

        // Apply alphabetical sorting if enabled (default)
        if (criteria.isSortAlphabetically()) {
            filtered.sort(Comparator.comparing(InternshipOpportunity::getTitle, String.CASE_INSENSITIVE_ORDER));
        }

        return filtered;
    }

    /**
     * Checks if an opportunity matches the filter criteria.
     * Used for incremental filtering or validation.
     */
    public boolean matchesCriteria(InternshipOpportunity opportunity, OpportunityFilterCriteria criteria) {
        if (criteria.getStatus() != null && opportunity.getStatus() != criteria.getStatus()) {
            return false;
        }

        if (criteria.getPreferredMajor() != null && !criteria.getPreferredMajor().trim().isEmpty()) {
            String majorFilter = criteria.getPreferredMajor().trim();
            String oppMajor = opportunity.getPreferredMajor();
            if (oppMajor == null || !oppMajor.trim().equalsIgnoreCase(majorFilter)) {
                return false;
            }
        }

        if (criteria.getLevel() != null && opportunity.getLevel() != criteria.getLevel()) {
            return false;
        }

        if (criteria.getClosingDateBefore() != null) {
            if (opportunity.getClosingDate() == null || 
                opportunity.getClosingDate().isAfter(criteria.getClosingDateBefore())) {
                return false;
            }
        }

        return true;
    }
}
