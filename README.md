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
Student student = (Student) userRepo.findById("U2310001A").get();
StudentActivityReport report = reportGen.generateStudentReport(student);
System.out.println(report.generateReport());
```

