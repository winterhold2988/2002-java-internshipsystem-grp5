# System Architecture - Internship Placement Management System

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER INTERFACE (CLI)                         │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │ LoginHandler │  │  StudentMenu │  │ CompanyRepresentative    │  │
│  │              │  │              │  │ Menu                     │  │
│  │ - login()    │  │ - display()  │  │                          │  │
│  │ - register() │  │ - apply()    │  │ - createOpportunity()    │  │
│  └──────────────┘  │ - withdraw() │  │ - reviewApplications()   │  │
│                    └──────────────┘  └──────────────────────────┘  │
│  ┌──────────────────────────┐  ┌─────────────────────────────────┐ │
│  │ CareerCenterStaffMenu    │  │ ConsoleUtil & InputValidator    │ │
│  │                          │  │ - Formatting & Validation       │ │
│  │ - approveReps()          │  └─────────────────────────────────┘ │
│  │ - approveOpportunities() │                                       │
│  │ - generateReports()      │                                       │
│  └──────────────────────────┘                                       │
└─────────────────────────────────────────────────────────────────────┘
                                    ↕
┌─────────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER                            │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────────────┐  ┌──────────────────────────────────────────┐ │
│  │ LoginService     │  │ InternshipService                        │ │
│  │ - authenticate() │  │ - listAvailableInternships()             │ │
│  └──────────────────┘  │ - createOpportunity()                    │ │
│                        │ - processApplication()                   │ │
│  ┌──────────────────┐  └──────────────────────────────────────────┘ │
│  │ ReportGenerator  │  ┌──────────────────────────────────────────┐ │
│  │ - generate()     │  │ DTOs & Filters                           │ │
│  │ - filter()       │  │ - OpportunityFilter, ApplicationFilter   │ │
│  └──────────────────┘  │ - ReportDTOs, Statistics                 │ │
│                        └──────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │ Utilities                                                     │   │
│  │ - DateTimeUtil, StringUtil, Logger, ErrorHandler             │   │
│  └──────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                    ↕
┌─────────────────────────────────────────────────────────────────────┐
│                       DATA ACCESS LAYER                              │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │ UserRepository   │  │ InternshipRepo   │  │ ApplicationRepo  │  │
│  │ - findById()     │  │ - findAll()      │  │ - findByStudent()│  │
│  │ - save()         │  │ - save()         │  │ - save()         │  │
│  │ - findAll()      │  │ - delete()       │  │ - update()       │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
│  ┌──────────────────┐  ┌──────────────────┐                         │
│  │ RegistrationRepo │  │ WithdrawalRepo   │                         │
│  │ - pending()      │  │ - pending()      │                         │
│  └──────────────────┘  └──────────────────┘                         │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │ Data Management                                               │   │
│  │ - DataBootstrap, DataPersistence, IDGenerator, Validator     │   │
│  └──────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                    ↕
┌─────────────────────────────────────────────────────────────────────┐
│                         DATA PERSISTENCE                             │
├─────────────────────────────────────────────────────────────────────┤
│  CSV Files in src/resources/data/:                                   │
│  - student_list.csv                                                  │
│  - staff_list.csv                                                    │
│  - company_representative_list.csv                                   │
│  - internship_system.log (logging)                                   │
└─────────────────────────────────────────────────────────────────────┘
```

## Component Interaction Flow

### 1. Application Startup

```
App.main()
    │
    ├──> initializeLogging()
    │    └──> Logger.setLogLevel(INFO)
    │
    ├──> new AppConfig()
    │    │
    │    ├──> Initialize Repositories
    │    │    ├──> UserRepository
    │    │    ├──> InternshipRepository
    │    │    ├──> ApplicationRepository
    │    │    ├──> RegistrationRequestRepository
    │    │    └──> WithdrawalRequestRepository
    │    │
    │    ├──> DataBootstrap.loadData()
    │    │    ├──> StudentDataLoader → CSV → Students
    │    │    ├──> StaffDataLoader → CSV → Staff
    │    │    └──> CompanyRepDataLoader → CSV → Reps
    │    │
    │    ├──> new DataPersistenceManager()
    │    ├──> new IDGenerator()
    │    └──> new ReportGenerator()
    │
    ├──> displayWelcomeBanner()
    ├──> displayDataSummary()
    │
    └──> runApplicationLoop()
         └──> [Main Loop - See below]
```

### 2. User Authentication Flow

```
LoginHandler.login()
    │
    ├──> Prompt for User ID
    │    └──> InputValidator.readNonEmptyString()
    │
    ├──> Check if "exit"
    │    └──> return null (exit)
    │
    ├──> Prompt for Password
    │    └──> InputValidator.readNonEmptyString()
    │
    ├──> UserRepository.findById(userId)
    │    │
    │    ├──> If not found
    │    │    └──> ErrorHandler.displayError()
    │    │
    │    └──> If found
    │         ├──> Check password match
    │         │    └──> user.getPassword().equals(password)
    │         │
    │         ├──> Check if Company Rep
    │         │    └──> Check isApproved()
    │         │
    │         └──> return User (success)
    │
    └──> Logger.info("User logged in")
```

### 3. Student Application Flow

```
StudentMenu.applyForInternship()
    │
    ├──> Display available opportunities
    │    │
    │    └──> InternshipRepository.findAll()
    │         ├──> Filter by student's major
    │         ├──> Filter by student's year (level restrictions)
    │         ├──> Filter by visibility == ON
    │         ├──> Filter by status == APPROVED
    │         └──> Filter out FILLED opportunities
    │
    ├──> Check application count
    │    └──> ApplicationRepository.findByStudentId()
    │         └──> If count >= 3
    │              └──> Display error, return
    │
    ├──> Prompt for Opportunity ID
    │    └──> InputValidator.readNonEmptyString()
    │
    ├──> Validate opportunity eligibility
    │    ├──> Check level vs year
    │    ├──> Check major match
    │    └──> Check available slots
    │
    ├──> Create Application
    │    ├──> Generate ID (IDGenerator.generateApplicationId())
    │    ├──> new InternshipApplication(...)
    │    ├──> Set status = PENDING
    │    └──> ApplicationRepository.save()
    │
    ├──> Logger.info("Application created")
    └──> Display success message
```

### 4. Company Representative - Create Opportunity Flow

```
CompanyRepMenu.createOpportunity()
    │
    ├──> Check opportunity count
    │    └──> InternshipRepository.findByCreatorId()
    │         └──> If count >= 5
    │              └──> Display error, return
    │
    ├──> Collect opportunity details
    │    ├──> Title (InputValidator.readNonEmptyString())
    │    ├──> Description (InputValidator.readNonEmptyString())
    │    ├──> Level (InputValidator.readInt(1-3))
    │    ├──> Preferred Major (InputValidator.readNonEmptyString())
    │    ├──> Opening Date (InputValidator.readDate())
    │    ├──> Closing Date (InputValidator.readFutureDate())
    │    └──> Slots (InputValidator.readInt(1-10))
    │
    ├──> Validate business rules
    │    ├──> Check closing > opening
    │    └──> Check slots <= 10
    │
    ├──> Create Opportunity
    │    ├──> Generate ID (IDGenerator.generateOpportunityId())
    │    ├──> new InternshipOpportunity(...)
    │    ├──> Set status = PENDING
    │    ├──> Set visibility = OFF
    │    └──> InternshipRepository.save()
    │
    ├──> Logger.info("Opportunity created")
    └──> Display success message
```

### 5. Company Rep - Review Applications Flow

```
CompanyRepMenu.reviewApplications()
    │
    ├──> Get all opportunities for this rep
    │    └──> InternshipRepository.findByCreatorId()
    │
    ├──> For each opportunity
    │    ├──> Get applications
    │    │    └──> ApplicationRepository.findByOpportunityId()
    │    │
    │    ├──> Display applications
    │    │    └──> ApplicationReportDTO.toDisplayString()
    │    │
    │    └──> Prompt for action
    │         ├──> Select application
    │         └──> Choose Approve/Reject
    │              │
    │              ├──> If APPROVE
    │              │    ├──> Set status = SUCCESSFUL
    │              │    └──> ApplicationRepository.update()
    │              │
    │              └──> If REJECT
    │                   ├──> Set status = UNSUCCESSFUL
    │                   └──> ApplicationRepository.update()
    │
    └──> Logger.info("Application reviewed")
```

### 6. Student - Accept Placement Flow

```
StudentMenu.acceptPlacement()
    │
    ├──> Get successful applications
    │    └──> ApplicationRepository.findByStudentId()
    │         └──> Filter status == SUCCESSFUL
    │
    ├──> Display successful applications
    │
    ├──> Prompt for selection
    │    └──> InputValidator.readInt()
    │
    ├──> Confirm acceptance
    │    └──> InputValidator.confirm()
    │
    ├──> Accept placement
    │    ├──> Set placementAccepted = true
    │    ├──> ApplicationRepository.update()
    │    │
    │    └──> Withdraw all other applications
    │         ├──> Get other applications
    │         ├──> Set status = WITHDRAWN
    │         └──> ApplicationRepository.update()
    │
    ├──> Check if opportunity filled
    │    ├──> Count accepted applications
    │    └──> If count == maxSlots
    │         ├──> Set opportunity status = FILLED
    │         ├──> Set visibility = OFF
    │         └──> InternshipRepository.update()
    │
    ├──> Logger.info("Placement accepted")
    └──> Display success message
```

### 7. Staff - Approve Opportunity Flow

```
StaffMenu.reviewOpportunities()
    │
    ├──> Get pending opportunities
    │    └──> InternshipRepository.findAll()
    │         └──> Filter status == PENDING
    │
    ├──> Display opportunities
    │    └──> OpportunityReportDTO.toDisplayString()
    │
    ├──> Prompt for selection
    │
    ├──> Confirm approval
    │    └──> InputValidator.confirm()
    │
    ├──> If APPROVE
    │    ├──> Set status = APPROVED
    │    ├──> Set visibility = ON
    │    └──> InternshipRepository.update()
    │
    ├──> If REJECT
    │    ├──> Set status = REJECTED
    │    └──> InternshipRepository.update()
    │
    ├──> Logger.info("Opportunity reviewed")
    └──> Display success message
```

### 8. Report Generation Flow

```
StaffMenu.generateReports()
    │
    ├──> Display report menu
    │    ├──> 1. System Statistics
    │    ├──> 2. Company Activity
    │    ├──> 3. Student Activity
    │    └──> 4. Filtered Lists
    │
    ├──> System Statistics
    │    ├──> ReportGenerator.generateSystemStatistics()
    │    ├──> Count users by role
    │    ├──> Count opportunities by status
    │    ├──> Count applications by status
    │    └──> Display formatted report
    │
    ├──> Company Activity
    │    ├──> Select company rep
    │    ├──> ReportGenerator.generateCompanyReport()
    │    ├──> List all opportunities
    │    ├──> List all applications received
    │    └──> Display statistics
    │
    ├──> Student Activity
    │    ├──> Select student
    │    ├──> ReportGenerator.generateStudentReport()
    │    ├──> List all applications
    │    ├──> Show placement status
    │    └──> Display statistics
    │
    └──> Filtered Lists
         ├──> Build filter
         │    └──> OpportunityFilter.builder()
         │         .status(...)
         │         .major(...)
         │         .level(...)
         │         .build()
         │
         ├──> ReportGenerator.generateOpportunityList(filter)
         └──> Display results
```

### 9. Application Exit Flow

```
App.saveDataOnExit()
    │
    ├──> DataPersistenceManager.exportAll()
    │    │
    │    ├──> exportStudents()
    │    │    └──> Write to student_list.csv
    │    │
    │    ├──> exportStaff()
    │    │    └──> Write to staff_list.csv
    │    │
    │    ├──> exportCompanyReps()
    │    │    └──> Write to company_representative_list.csv
    │    │
    │    ├──> exportOpportunities()
    │    │    └──> Write to opportunities.csv
    │    │
    │    └──> exportApplications()
    │         └──> Write to applications.csv
    │
    ├──> Logger.info("Data saved")
    │
    ├──> displayExitMessage()
    │
    └──> CLIUtil.closeScanner()
```

## Design Patterns Implementation

### 1. Repository Pattern
**Purpose**: Abstract data access
```java
interface Repository<T> {
    Optional<T> findById(String id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(String id);
}
```

### 2. Builder Pattern
**Purpose**: Complex object construction
```java
OpportunityFilter filter = OpportunityFilter.builder()
    .status(APPROVED)
    .major("Computer Science")
    .availableOnly(true)
    .build();
```

### 3. Factory Pattern
**Purpose**: Object creation
```java
ReportGenerator.generateSystemStatistics()
ReportGenerator.generateCompanyReport(rep)
ReportGenerator.generateStudentReport(student)
```

### 4. Template Method Pattern
**Purpose**: Menu structure
```java
abstract class MenuBase {
    public final void display() {
        printHeader();
        printOptions();
        handleChoice();
    }
    
    protected abstract void printOptions();
    protected abstract void handleChoice();
}
```

### 5. Strategy Pattern
**Purpose**: Filtering algorithms
```java
Stream<Opportunity> opportunities = repository.findAll().stream();
opportunities = filter.apply(opportunities);
```

## State Transitions

### Opportunity Status
```
PENDING ──[Staff Approves]──> APPROVED ──[All slots filled]──> FILLED
   │
   └──[Staff Rejects]──> REJECTED
```

### Application Status
```
PENDING ──[Company Approves]──> SUCCESSFUL ──[Student Accepts]──> (placementAccepted = true)
   │
   ├──[Company Rejects]──> UNSUCCESSFUL
   │
   └──[Student Withdraws]──> WITHDRAWN
```

### Registration Request
```
PENDING ──[Staff Approves]──> APPROVED (Rep can login)
   │
   └──[Staff Rejects]──> REJECTED (Rep blocked)
```

### Withdrawal Request
```
PENDING ──[Staff Approves]──> APPROVED (Application withdrawn)
   │
   └──[Staff Rejects]──> REJECTED (Application stays)
```

## Error Handling Strategy

```
Try-Catch Hierarchy:
    │
    ├──> InternshipSystemException (Base)
    │    │
    │    ├──> ValidationException
    │    │    └──> Invalid input, format errors
    │    │
    │    ├──> DataNotFoundException
    │    │    └──> Entity not found
    │    │
    │    ├──> DuplicateEntryException
    │    │    └──> Duplicate ID, email
    │    │
    │    ├──> UnauthorizedException
    │    │    └──> Permission denied
    │    │
    │    └──> BusinessRuleException
    │         └──> Rule violations
    │
    └──> ErrorHandler.handleException()
         ├──> Log to file
         ├──> Display user message
         └──> Suggest recovery
```

## Threading & Concurrency

Currently single-threaded, but designed for future expansion:
- IDGenerator uses AtomicInteger (thread-safe)
- Repository operations are synchronized
- Ready for concurrent access patterns

## Performance Considerations

- **Lazy Loading**: Streams for filtering
- **Caching**: In-memory repositories
- **Indexing**: HashMap for O(1) lookups
- **Batch Operations**: exportAll() for efficiency

## Security Measures

- Password validation (not "password")
- Session management (login/logout)
- Role-based access control
- Approval workflows
- Audit logging

---

**System Version**: 1.0  
**Last Updated**: 2025-11-13  
**Total Components**: 50+ classes, 8,000+ LOC
