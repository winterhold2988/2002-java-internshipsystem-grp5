# Internship Placement Management System Skeleton

This repository provides a starting point for the SC/CE/CZ2002 assignment. It includes
package scaffolding, core domain models, repositories, service shells, and CLI menus to
organise future work. The goal is to let the team focus on feature implementation,
testing, and documentation.

## Project structure

```
src/
  main/
    java/edu/ntu/ccds/sc2002/internship/
      App.java                     # Application entry point
      cli/                         # CLI orchestration layer
      config/                      # Wiring of repositories/services
      data/                        # Data loaders and bootstrap scripts
      dto/                         # Filter/report transport objects
      enums/                       # Shared enumerations
      model/                       # Domain entities and aggregates
      repository/                  # In-memory repositories (replaceable)
      service/                     # Business logic services
      util/                        # Shared helpers for CLI usage
    resources/data/                # Seed data samples (CSV)
  test/
    java/                          # Reserved for unit tests
```


## Sample data files

The CSV files under `src/main/resources/data` are intentionally lightweight:

- `student_list.csv` — `StudentID,Name,Major,Year,Email`
- `staff_list.csv` — `StaffID,Name,Role,Department,Email`
- `company_representative_list.csv` — `CompanyRepID,Name,CompanyName,Department,Position,Email,Status`

Extend or replace these datasets when wiring up your loaders. Default passwords are not
stored in the files; assign them during bootstrap (spec states the default is `password`).

## Data Layer Implementation

The `data/` package contains classes responsible for loading, persisting, and managing data:

### Core Data Classes

1. **CSVReader** - Utility for reading CSV files from resources or file paths
   - `readFromResources(String)` - Reads CSV from resources directory
   - `readFromPath(String)` - Reads CSV from absolute file path

2. **StudentDataLoader** - Loads student data from `student_list.csv`
   - Default password: "password"
   - Format: StudentID, Name, Major, Year, Email

3. **StaffDataLoader** - Loads career center staff from `staff_list.csv`
   - Default password: "password"
   - Format: StaffID, Name, Role, Department, Email

4. **CompanyRepresentativeDataLoader** - Loads company representatives from `company_representative_list.csv`
   - Default password: "password"
   - Parses approval status from CSV
   - Format: CompanyRepID, Name, CompanyName, Department, Position, Email, Status

5. **DataBootstrap** - Coordinates initialization of all data at application startup
   - Loads all user types from CSV files
   - Populates repositories with initial data
   - Prints summary of loaded data

6. **DataPersistenceManager** - Handles exporting data back to CSV files
   - `exportStudents(String)` - Exports student data
   - `exportStaff(String)` - Exports staff data
   - `exportCompanyRepresentatives(String)` - Exports company representatives
   - `exportInternshipOpportunities(String)` - Exports opportunities
   - `exportInternshipApplications(String)` - Exports applications
   - `exportAll(String)` - Exports all data to specified directory
   
   **Persistence Strategy**:
   - Original CSV files in `src/resources/data/` remain unchanged (seed data preservation)
   - Runtime changes are exported to separate files with `_export` suffix
   - Export files: `student_list_export.csv`, `staff_list_export.csv`, `company_representative_list_export.csv`, etc.
   - Exports are triggered automatically when the application exits
   - To persist changes permanently, manually rename export files to replace originals
   - This design allows testing without corrupting original seed data

7. **IDGenerator** - Generates unique IDs for runtime entity creation
   - `generateApplicationId()` - Format: APP0001, APP0002, etc.
   - `generateOpportunityId()` - Format: OPP0001, OPP0002, etc.
   - `generateRequestId()` - Format: REQ0001, REQ0002, etc.
   - `generateWithdrawalId()` - Format: WDR0001, WDR0002, etc.
   - Automatically detects existing IDs to avoid conflicts

8. **DataValidator** - Validation utilities for data integrity
   - Email validation
   - Student ID validation (format: U1234567A)
   - Staff ID validation (format: abc123)
   - Year of study validation (1-4)
   - General field validation

9. **DataStatistics** - Generates reports and statistics about system data
   - User counts by type
   - Opportunity counts by status
   - Application counts by status
   - Pending request counts
   - `generateReport()` - Creates comprehensive statistics report

### Integration with AppConfig

The `AppConfig` class has been updated to wire together all repositories and data utilities:

```java
AppConfig config = new AppConfig(); // Initializes repositories and loads CSV data
UserRepository userRepo = config.getUserRepository();
IDGenerator idGen = config.getIdGenerator();
DataPersistenceManager persist = config.getDataPersistenceManager();
```

### Usage Examples

**Loading data at startup:**
```java
// Automatic - happens in AppConfig constructor
AppConfig config = new AppConfig();
```

**Generating unique IDs:**
```java
IDGenerator idGen = config.getIdGenerator();
String appId = idGen.generateApplicationId(); // "APP0001"
String oppId = idGen.generateOpportunityId(); // "OPP0001"
```

**Exporting data:**
```java
DataPersistenceManager persist = config.getDataPersistenceManager();
persist.exportAll("./exports/"); // Exports all data to exports directory
```

**Viewing statistics:**
```java
DataStatistics stats = new DataStatistics(
    userRepo, internshipRepo, applicationRepo, regReqRepo, withdrawalRepo);
stats.printReport(); // Prints comprehensive statistics
```

## DTO (Data Transfer Object) Layer

The `dto/` package contains filter and report classes for querying and displaying data:

### Filter Classes

Filters use the **Builder Pattern** for flexible, readable query construction:

1. **InternshipOpportunityFilter** - Filter opportunities by multiple criteria
   - Company name, status, level, major
   - Visibility, availability
   - Date ranges (opening/closing)
   - Minimum available slots
   - Created by (company rep ID)
   - Title contains (text search)

2. **InternshipApplicationFilter** - Filter applications by criteria
   - Student ID, opportunity ID
   - Application status, placement acceptance
   - Submission date range
   - Company name, student major

3. **UserFilter** - Filter users by various attributes
   - Role (student, staff, company rep)
   - Name contains (text search)
   - Approval status (for company reps)
   - Department (for staff)
   - Major and year (for students)
   - Company name (for company reps)

### Report DTOs

Report DTOs convert entities into display-friendly formats with summary methods:

1. **OpportunityReportDTO** - Opportunity summary for reports
   - All key opportunity details
   - Calculated fields (available slots)
   - `toDisplayString()` - Full formatted display
   - `toSummaryString()` - One-line summary
   - `from(InternshipOpportunity)` - Factory method

2. **ApplicationReportDTO** - Application summary for reports
   - Student and opportunity details
   - Application status and timestamps
   - `toDisplayString()` - Full formatted display
   - `toSummaryString()` - One-line summary
   - `from(InternshipApplication)` - Factory method

3. **UserReportDTO** - User summary for reports
   - User details with role-specific information
   - `toDisplayString()` - Full formatted display
   - `toSummaryString()` - One-line summary
   - `from(User)` - Factory method

### Comprehensive Report Classes

1. **SystemStatisticsReport** - System-wide statistics
   - User counts by type
   - Opportunity and application statistics by status
   - Slot utilization metrics
   - Pending requests counts
   - `generateReport()` - Creates formatted ASCII table report

2. **CompanyActivityReport** - Company-specific report
   - All opportunities posted by company
   - All applications received
   - Slot statistics and fill rates
   - Pending and successful application counts
   - `generateReport()` - Creates formatted company report

3. **StudentActivityReport** - Student-specific report
   - Student profile information
   - All applications submitted
   - Application status breakdown
   - Placement acceptance status
   - `generateReport()` - Creates formatted student report

### Report Generator

**ReportGenerator** - Central report generation utility that bridges repositories and DTOs:

```java
ReportGenerator reportGen = config.getReportGenerator();

// Generate system-wide statistics
SystemStatisticsReport sysStats = reportGen.generateSystemStatistics();
System.out.println(sysStats.generateReport());

// Generate company-specific report
CompanyActivityReport companyReport = reportGen.generateCompanyReport(companyRep);
System.out.println(companyReport.generateReport());

// Generate student-specific report
StudentActivityReport studentReport = reportGen.generateStudentReport(student);
System.out.println(studentReport.generateReport());

// Generate filtered lists
InternshipOpportunityFilter filter = InternshipOpportunityFilter.builder()
    .status(OpportunityStatus.APPROVED)
    .availableOnly(true)
    .preferredMajor("Computer Science")
    .build();
List<OpportunityReportDTO> opportunities = reportGen.generateOpportunityList(filter);
```

### Usage Examples

**Filtering opportunities:**
```java
// Find all available CS opportunities from a specific company
InternshipOpportunityFilter filter = InternshipOpportunityFilter.builder()
    .companyName("TechWave Pte Ltd")
    .preferredMajor("Computer Science")
    .availableOnly(true)
    .status(OpportunityStatus.APPROVED)
    .build();

List<OpportunityReportDTO> results = reportGen.generateOpportunityList(filter);
results.forEach(opp -> System.out.println(opp.toSummaryString()));
```

**Filtering applications:**
```java
// Find all pending applications for a student
InternshipApplicationFilter filter = InternshipApplicationFilter.builder()
    .studentId("U2310001A")
    .status(ApplicationStatus.PENDING)
    .build();

List<ApplicationReportDTO> apps = reportGen.generateApplicationList(filter);
apps.forEach(app -> System.out.println(app.toDisplayString()));
```

**Filtering users:**
```java
// Find all approved company representatives
UserFilter filter = UserFilter.builder()
    .role(UserRole.COMPANY_REPRESENTATIVE)
    .approved(true)
    .build();

List<UserReportDTO> users = reportGen.generateUserList(filter);
users.forEach(user -> System.out.println(user.toSummaryString()));
```

**Generating comprehensive reports:**
```java
// System statistics
ReportGenerator reportGen = config.getReportGenerator();
SystemStatisticsReport stats = reportGen.generateSystemStatistics();
System.out.println(stats.generateReport());

// Company activity
CompanyRepresentative rep = (CompanyRepresentative) userRepo.findById("jane.ong@techwave.com").get();
CompanyActivityReport report = reportGen.generateCompanyReport(rep);
System.out.println(report.generateReport());

// Student activity
// Student activity
Student student = (Student) userRepo.findById("U2310001A").get();
StudentActivityReport report = reportGen.generateStudentReport(student);
System.out.println(report.generateReport());
```

## Utility Layer Implementation

The `util/` package provides shared helper classes, error handling, and logging utilities:

### Core Utility Classes

1. **DateTimeUtil** - Date and time utilities
   - `parseDate(String)` - Parses dates in dd/MM/yyyy format
   - `parseDateTime(String)` - Parses date-times in dd/MM/yyyy HH:mm:ss format
   - `formatDate(LocalDate)` - Formats dates for display
   - `formatDateTime(LocalDateTime)` - Formats date-times for display
   - `isValidDateFormat(String)` - Validates date format
   - `isInPast(LocalDate)` - Checks if date is in the past
   - `isInFuture(LocalDate)` - Checks if date is in the future
   - `isToday(LocalDate)` - Checks if date is today
   - `isWithinRange(LocalDate, LocalDate, LocalDate)` - Checks if date is within range
   - `daysBetween(LocalDate, LocalDate)` - Calculates days between dates

2. **StringUtil** - String manipulation and validation
   - `isNullOrEmpty(String)` - Null-safe empty check
   - `isNotEmpty(String)` - Null-safe non-empty check
   - `trim(String)` - Null-safe trim
   - `capitalize(String)` - Capitalizes first letter
   - `capitalizeWords(String)` - Capitalizes all words
   - `truncate(String, int)` - Truncates with ellipsis
   - `padRight(String, int)` - Right-padding
   - `padLeft(String, int)` - Left-padding
   - `repeat(String, int)` - Repeats string
   - `isAlphabetic(String)` - Checks for letters only
   - `isNumeric(String)` - Checks for digits only
   - `isAlphanumeric(String)` - Checks for letters and digits
   - `removeWhitespace(String)` - Removes all whitespace
   - `toSafeFilename(String)` - Converts to safe filename
   - `mask(String, int)` - Masks sensitive data
   - `toSnakeCase(String)` - Converts to snake_case
   - `toCamelCase(String)` - Converts to camelCase

3. **InputValidator** - Console input validation
   - `readInt(Scanner, int, int, String)` - Reads integer within range
   - `readPositiveInt(Scanner, String)` - Reads positive integer
   - `readNonNegativeInt(Scanner, String)` - Reads non-negative integer
   - `readBoolean(Scanner)` - Reads yes/no input
   - `readNonEmptyString(Scanner, String)` - Reads required string
   - `readPattern(Scanner, String, String, String)` - Reads string matching regex
   - `readEmail(Scanner)` - Reads and validates email
   - `readDate(Scanner, String)` - Reads date in dd/MM/yyyy format
   - `readFutureDate(Scanner, String)` - Reads future date only
   - `readDateInRange(Scanner, String, LocalDate, LocalDate)` - Reads date within range
   - `isValidIdFormat(String, String)` - Validates ID format
   - `isValidPhoneNumber(String)` - Validates phone number
   - `isValidYear(int)` - Validates university year (1-4)
   - `confirm(Scanner, String)` - Yes/no confirmation
   - `readMenuChoice(Scanner, int, int)` - Reads menu selection

4. **ConsoleUtil** - Console formatting and display
   - `printHeader(String)` - Prints formatted header
   - `printSeparator()` - Prints separator line
   - `printThickSeparator()` - Prints thick separator
   - `centerText(String, int)` - Centers text
   - `printSuccess(String)` - Prints success message with prefix
   - `printError(String)` - Prints error message with prefix
   - `printWarning(String)` - Prints warning message with prefix
   - `printInfo(String)` - Prints info message with prefix
   - `printTable(String[], List<String[]>, int[])` - Prints formatted table
   - `printNumberedList(List<String>)` - Prints numbered list
   - `printBulletedList(List<String>)` - Prints bulleted list
   - `printKeyValue(String, String)` - Prints key-value pair
   - `printProgressBar(int, int, int)` - Prints progress bar
   - `printBox(String)` - Prints text in box
   - `formatMenuOption(int, String)` - Formats menu option

5. **Logger** - Logging utility
   - `setLogLevel(LogLevel)` - Sets minimum log level (DEBUG, INFO, WARN, ERROR)
   - `setConsoleOutput(boolean)` - Enables/disables console logging
   - `setFileOutput(boolean)` - Enables/disables file logging
   - `debug(String)` - Logs debug message
   - `info(String)` - Logs info message
   - `warn(String)` - Logs warning message
   - `error(String)` - Logs error message
   - `error(String, Throwable)` - Logs error with exception
   - `entering(String, String)` - Logs method entry
   - `exiting(String, String)` - Logs method exit
   - Logs written to `internship_system.log`

6. **ErrorHandler** - Centralized error handling
   - `handleException(Exception)` - Handles any exception with logging and user message
   - `handleException(Exception, String)` - Handles exception with custom message
   - `handleValidationError(String, String)` - Handles validation errors
   - `displayError(String)` - Displays error to user
   - `displayWarning(String)` - Displays warning to user
   - `displayInfo(String)` - Displays info to user
   - `displaySuccess(String)` - Displays success to user
   - `safeExecute(RiskyOperation, String)` - Wraps risky operations with error handling

### Exception Hierarchy

The `util.exception/` package provides custom exceptions:

1. **InternshipSystemException** - Base exception for all system exceptions
2. **ValidationException** - Thrown when validation fails
3. **DataNotFoundException** - Thrown when requested data is not found
4. **DuplicateEntryException** - Thrown when duplicate entry is detected
5. **UnauthorizedException** - Thrown when unauthorized action is attempted
6. **BusinessRuleException** - Thrown when business rule is violated

### Usage Examples

**Date and time handling:**
```java
// Parse and validate dates
String dateStr = "25/12/2024";
Optional<LocalDate> date = DateTimeUtil.parseDate(dateStr);

// Check date validity
if (DateTimeUtil.isInFuture(date.get())) {
    System.out.println("Date is in the future");
}

// Calculate days between
long days = DateTimeUtil.daysBetween(LocalDate.now(), date.get());
```

**String manipulation:**
```java
// Safe string operations
String name = StringUtil.trim(input);
if (StringUtil.isNotEmpty(name)) {
    String formatted = StringUtil.capitalizeWords(name);
}

// Format for display
String title = StringUtil.truncate(longTitle, 50);
String padded = StringUtil.padRight(title, 60);
```

**Console input validation:**
```java
Scanner scanner = new Scanner(System.in);

// Read validated integer
System.out.print("Enter year (1-4): ");
int year = InputValidator.readInt(scanner, 1, 4, "Invalid year");

// Read email
System.out.print("Enter email: ");
String email = InputValidator.readEmail(scanner);

// Confirm action
if (InputValidator.confirm(scanner, "Are you sure?")) {
    // Proceed
}
```

**Console formatting:**
```java
// Print formatted header
ConsoleUtil.printHeader("Internship Management System");

// Print table
String[] headers = {"ID", "Name", "Status"};
List<String[]> rows = Arrays.asList(
    new String[]{"OPP0001", "Software Engineer", "Open"},
    new String[]{"OPP0002", "Data Analyst", "Closed"}
);
int[] widths = {10, 30, 10};
ConsoleUtil.printTable(headers, rows, widths);

// Print status messages
ConsoleUtil.printSuccess("Application submitted successfully");
ConsoleUtil.printError("Invalid input");
```

**Error handling:**
```java
// Handle exceptions
try {
    // Risky operation
} catch (Exception e) {
    ErrorHandler.handleException(e);
}

// Safe execution
boolean success = ErrorHandler.safeExecute(() -> {
    // Code that might throw exception
}, "Failed to process request");

// Display messages
ErrorHandler.displaySuccess("Operation completed");
ErrorHandler.displayError("Operation failed");
```

**Logging:**
```java
// Configure logging
Logger.setLogLevel(Logger.LogLevel.INFO);
Logger.setConsoleOutput(true);
Logger.setFileOutput(true);

// Log messages
Logger.info("Application started");
Logger.debug("Processing user input");
Logger.warn("Invalid configuration detected");
Logger.error("Failed to load data", exception);

// Log method execution
Logger.entering("UserRepository", "findById");
Logger.exiting("UserRepository", "findById", user);
```

## CLI pattern
```

