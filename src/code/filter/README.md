# Filter Package Documentation

## Overview
The `filter` package provides a comprehensive, persistent filtering system for internship opportunities. All user types (Students, Company Representatives, Career Center Staff) can configure filters that persist across menu navigation within a session.

## Architecture

### SOLID Principles Applied

**Single Responsibility Principle (SRP)**
- `OpportunityFilterCriteria`: Responsible only for holding filter settings
- `OpportunityFilterService`: Responsible only for applying filters
- `UserFilterState`: Responsible only for storing one user's filter state
- `FilterStateManager`: Responsible only for managing filter states across users

**Open/Closed Principle (OCP)**
- New filter criteria can be added to `OpportunityFilterCriteria.Builder` without modifying existing code
- New filtering logic can be added to `OpportunityFilterService` without breaking existing filters

**Dependency Inversion Principle (DIP)**
- `FilterStateManager` could be easily extended to persist to database instead of in-memory storage
- Services depend on filter abstractions, not concrete implementations

**Immutability Pattern**
- `OpportunityFilterCriteria` is immutable (all fields final, no setters)
- Thread-safe and prevents accidental modification

## Components

### 1. OpportunityFilterCriteria
- Immutable value object holding filter settings
- Builder pattern for flexible construction
- Supports filtering by:
  - Status (PENDING, APPROVED, REJECTED, FILLED)
  - Preferred Major (case-insensitive matching)
  - Internship Level (BASIC, INTERMEDIATE, ADVANCED)
  - Closing Date (opportunities closing before specified date)
  - Alphabetical sorting (enabled by default)

### 2. OpportunityFilterService
- Stateless service that applies filters to opportunity lists
- Methods:
  - `filterOpportunities()`: Applies all criteria and sorts results
  - `matchesCriteria()`: Checks if single opportunity matches criteria
- Filters are applied sequentially via streams
- Default: Alphabetical sorting by opportunity title

### 3. UserFilterState
- Stores filter preferences for a single user
- Mutable (allows updating filter criteria)
- Default state: Alphabetical sorting, no filters active

### 4. FilterStateManager
- Manages filter states for all users (in-memory during session)
- Thread-safe using ConcurrentHashMap
- Methods:
  - `getFilterState()`: Retrieves or creates user's filter state
  - `updateFilterCriteria()`: Updates user's filters
  - `clearFilters()`: Resets to default (alphabetical only)
  - `getFilterCriteria()`: Gets current criteria for a user
  - `removeUserState()`: Cleanup on logout

## Integration

### AppConfig
- Initializes `OpportunityFilterService` and `FilterStateManager`
- Provides singleton instances to all menus via dependency injection

### MenuBase
- All menus inherit filter components
- Provides `configureFilters()` and `clearFilters()` methods
- Interactive UI for setting filter criteria

### Menu Classes
- **StudentMenu**: Filters available internships (after year/major filtering)
- **CompanyRepresentativeMenu**: Filters "My Opportunities"
- **CareerCenterStaffMenu**: Filters "All Opportunities"

## Usage Flow

1. User logs in → `FilterStateManager` creates default state (alphabetical sorting)
2. User navigates to any menu → Filter state persists
3. User selects "Configure Filters" → Interactive UI to set criteria
4. User views opportunities → Filters applied automatically to listings
5. User switches menus → Filters remain active
6. User selects "Clear Filters" → Resets to default state
7. User logs out → Filter state can be cleaned up (optional)

## Filter Behavior

### Default Behavior
- All opportunities sorted alphabetically by title
- No filters active (shows all eligible opportunities)

### With Active Filters
- Filters shown at top of opportunity listings
- Empty results if no opportunities match criteria
- Filters persist across "View" and "Apply" operations

### Filter Combinations
- All filters are AND conditions (opportunity must match all criteria)
- Empty/null filter values are ignored (no filtering for that criterion)

## Example Usage

```java
// User configures filters
OpportunityFilterCriteria criteria = OpportunityFilterCriteria.builder()
    .status(OpportunityStatus.APPROVED)
    .preferredMajor("Computer Science")
    .level(InternshipLevel.BASIC)
    .closingDateBefore(LocalDate.of(2025, 12, 31))
    .sortAlphabetically(true)
    .build();

// Save for user
filterStateManager.updateFilterCriteria(userId, criteria);

// Apply filters to opportunities
List<InternshipOpportunity> filtered = opportunityFilterService
    .filterOpportunities(allOpportunities, criteria);

// Check active filters
if (filterStateManager.hasActiveFilters(userId)) {
    System.out.println(criteria.toString());
}
```

## Testing Scenarios

1. **Student Scenario**: Set major=CS, level=BASIC → Should see only BASIC CS opportunities
2. **Company Rep Scenario**: Set status=APPROVED → Should see only approved opportunities they created
3. **Staff Scenario**: Set closing date → Should see opportunities closing before that date
4. **Persistence Test**: Set filters, navigate to different menu, come back → Filters still active
5. **Clear Test**: Clear filters → Back to alphabetical list of all opportunities

## Performance Considerations

- Filtering uses Java Streams (lazy evaluation)
- Sorting happens once after filtering
- Thread-safe ConcurrentHashMap for multi-user scenarios
- No database calls (in-memory filtering)

## Future Enhancements

- Persist filter preferences to database/file
- Add more filter criteria (company name, date posted, etc.)
- Save multiple filter presets per user
- Export filtered results to CSV
- Add date range filters (opening date, closing date range)
