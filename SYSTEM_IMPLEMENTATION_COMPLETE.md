# Internship Placement Management System - Complete Implementation

## System Overview

This is a comprehensive Command-Line Interface (CLI) application for managing internship placements at Nanyang Technological University (NTU). The system serves as a centralized hub for Students, Company Representatives, and Career Center Staff.

## System Architecture

### Three-Tier Architecture

```
┌─────────────────────────────────────────────┐
│         Presentation Layer (CLI)             │
│  - LoginHandler, MainMenu, Role-based Menus │
│  - ConsoleUtil, InputValidator              │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│         Business Logic Layer                │
│  - Services (Internship, Login)             │
│  - ReportGenerator, DTOs, Filters           │
│  - ErrorHandler, Logger                     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│         Data Access Layer                   │
│  - Repositories (User, Internship, etc.)    │
│  - DataBootstrap, DataPersistence           │
│  - IDGenerator, DataValidator               │
└─────────────────────────────────────────────┘
```

## User Types & Capabilities

### 1. All Users (Base Capabilities)
- **User ID, Name, Password**
- **Login/Logout** - Secure authentication
- **Change Password** - Password management
- **Default Password**: `password`

### 2. Students (ID Format: U1234567A)

**Automatic Registration**
- Loaded from `student_list.csv` at initialization
- Fields: StudentID, Name, Major, Year (1-4), Email

**View Internship Opportunities**
- Filtered by Year of Study and Major
- Only visible opportunities (visibility = ON)
- Opportunities must be APPROVED and not FILLED

**Apply for Internships**
- Maximum 3 concurrent applications
- Year 1-2: BASIC level only
- Year 3-4: All levels (BASIC, INTERMEDIATE, ADVANCED)
- Automatic major matching

**Track Applications**
- View all applications (even if visibility OFF)
- Status: PENDING → SUCCESSFUL/UNSUCCESSFUL
- Can accept ONE placement confirmation
- All other applications withdrawn upon acceptance

**Withdrawal Requests**
- Request withdrawal before/after placement
- Subject to Career Center Staff approval

### 3. Company Representatives (ID: Email Address)

**Registration Process**
- Must register with company details
- Approval required from Career Center Staff
- Fields: Name, Company, Department, Position, Email

**Create Internship Opportunities (Max 5)**
- Internship Title
- Description
- Level (BASIC, INTERMEDIATE, ADVANCED)
- Preferred Major (1 major)
- Opening Date & Closing Date
- Number of Slots (max 10)
- Status: PENDING → APPROVED → FILLED

**Manage Applications**
- View all applications for their opportunities
- View student details
- APPROVE or REJECT applications
- Opportunity becomes FILLED when all slots confirmed

**Visibility Control**
- Toggle opportunity visibility ON/OFF
- Controls student view access

### 4. Career Center Staff (ID: NTU Account)

**Automatic Registration**
- Loaded from `staff_list.csv` at initialization
- Fields: StaffID, Name, Role, Department, Email

**Approve Company Representatives**
- Authorize or reject registration requests
- Enable/disable company rep accounts

**Approve Internship Opportunities**
- Review opportunities from companies
- APPROVE or REJECT submissions
- Approved opportunities become visible to students

**Manage Withdrawal Requests**
- Approve or reject student withdrawals
- Before or after placement confirmation

**Generate Reports**
- System-wide statistics
- Filter by Status, Major, Level, Closing Date
- Company activity reports
- Student activity reports

## Data Flow

### Application Lifecycle

```
1. COMPANY CREATES OPPORTUNITY
   Status: PENDING
   Visibility: OFF
   ↓
2. STAFF APPROVES OPPORTUNITY
   Status: APPROVED
   Visibility: ON
   ↓
3. STUDENT APPLIES
   Application Status: PENDING
   ↓
4. COMPANY REVIEWS
   Application Status: SUCCESSFUL/UNSUCCESSFUL
   ↓
5. STUDENT ACCEPTS PLACEMENT
   - Other applications withdrawn
   - Slot confirmed
   ↓
6. ALL SLOTS FILLED
   Opportunity Status: FILLED
   Visibility: OFF (automatic)
```

### Withdrawal Flow

```
STUDENT REQUESTS WITHDRAWAL
   ↓
WITHDRAWAL REQUEST: PENDING
   ↓
STAFF REVIEWS
   ↓
APPROVED → Application withdrawn
REJECTED → Application remains active
```

## Technical Implementation

### Package Structure

```
code/
├── App.java                          # Main entry point
├── cli/                              # Command-line interface
│   ├── LoginHandler.java             # User authentication
│   ├── MainMenu.java                 # Main menu router
│   ├── StudentMenu.java              # Student features
│   ├── CompanyRepresentativeMenu.java # Company features
│   ├── CareerCenterStaffMenu.java    # Staff features
│   ├── CLIUtil.java                  # CLI utilities
│   └── MenuBase.java                 # Base menu class
├── config/
│   └── AppConfig.java                # Application configuration
├── data/                             # Data management
│   ├── CSVReader.java                # CSV parsing
│   ├── DataBootstrap.java            # Initial data loading
│   ├── DataPersistenceManager.java   # Data export
│   ├── IDGenerator.java              # Unique ID generation
│   ├── DataValidator.java            # Data validation
│   └── DataStatistics.java           # Statistics
├── dto/                              # Data transfer objects
│   ├── *Filter.java                  # Filter builders
│   ├── *ReportDTO.java               # Report DTOs
│   └── ReportGenerator.java          # Report factory
├── enums/                            # Enumerations
│   ├── UserRole.java                 # User types
│   ├── ApplicationStatus.java        # Application states
│   ├── OpportunityStatus.java        # Opportunity states
│   ├── InternshipLevel.java          # Internship levels
│   └── WithdrawalDecision.java       # Withdrawal decisions
├── model/                            # Domain models
│   ├── User.java                     # Base user
│   ├── Student.java                  # Student entity
│   ├── CompanyRepresentative.java    # Company rep entity
│   ├── CareerCenterStaff.java        # Staff entity
│   ├── InternshipOpportunity.java    # Opportunity entity
│   ├── InternshipApplication.java    # Application entity
│   ├── RegistrationRequest.java      # Registration entity
│   └── WithdrawalRequest.java        # Withdrawal entity
├── repository/                       # Data repositories
│   ├── UserRepository.java           # User CRUD
│   ├── InternshipRepository.java     # Opportunity CRUD
│   ├── ApplicationRepository.java    # Application CRUD
│   ├── RegistrationRequestRepository.java # Registration CRUD
│   └── WithdrawalRequestRepository.java   # Withdrawal CRUD
├── service/                          # Business logic
│   ├── LoginService.java             # Authentication
│   └── IntershipService.java         # Internship operations
└── util/                             # Utilities
    ├── DateTimeUtil.java             # Date/time handling
    ├── StringUtil.java               # String operations
    ├── InputValidator.java           # Input validation
    ├── ConsoleUtil.java              # Console formatting
    ├── Logger.java                   # Logging
    ├── ErrorHandler.java             # Error handling
    └── exception/                    # Custom exceptions
        ├── InternshipSystemException.java
        ├── ValidationException.java
        ├── DataNotFoundException.java
        ├── DuplicateEntryException.java
        ├── UnauthorizedException.java
        └── BusinessRuleException.java
```

### Design Patterns Used

1. **Repository Pattern** - Data access abstraction
2. **Builder Pattern** - Filter and report DTOs
3. **Factory Pattern** - ReportGenerator
4. **Singleton Pattern** - AppConfig
5. **Template Method** - MenuBase
6. **Strategy Pattern** - Filtering
7. **Utility Pattern** - Helper classes

### Key Features

#### Filtering System
```java
// Example: Find all APPROVED CS opportunities
InternshipOpportunityFilter filter = InternshipOpportunityFilter.builder()
    .status(OpportunityStatus.APPROVED)
    .preferredMajor("Computer Science")
    .availableOnly(true)
    .build();

List<OpportunityReportDTO> opportunities = reportGenerator.generateOpportunityList(filter);
```

#### Report Generation
```java
// System statistics
SystemStatisticsReport stats = reportGenerator.generateSystemStatistics();
System.out.println(stats.generateReport());

// Company activity
CompanyActivityReport report = reportGenerator.generateCompanyReport(companyRep);

// Student activity
StudentActivityReport report = reportGenerator.generateStudentReport(student);
```

#### Data Persistence
```java
// Save all data to CSV
dataPersistenceManager.exportAll("src/resources/data");

// Individual exports
dataPersistenceManager.exportStudents("src/resources/data/students.csv");
dataPersistenceManager.exportInternshipOpportunities("src/resources/data/opportunities.csv");
```

#### ID Generation
```java
String appId = idGenerator.generateApplicationId();  // APP0001
String oppId = idGenerator.generateOpportunityId();  // OPP0001
String reqId = idGenerator.generateRequestId();      // REQ0001
String wdrId = idGenerator.generateWithdrawalId();   // WDR0001
```

## Business Rules

### Application Rules
1. Students can have max 3 active applications
2. Year 1-2 students: BASIC level only
3. Year 3-4 students: All levels
4. Applications match student's major with opportunity's preferred major
5. Only 1 placement can be accepted
6. Accepting placement withdraws all other applications

### Opportunity Rules
1. Max 5 opportunities per company
2. Max 10 slots per opportunity
3. Must be APPROVED before visible
4. Becomes FILLED when all slots confirmed
5. Cannot apply after closing date
6. Cannot apply if status is FILLED

### Withdrawal Rules
1. Can withdraw before or after placement
2. Requires staff approval
3. APPROVED = application removed
4. REJECTED = application remains

## Data Files

### student_list.csv
```csv
StudentID,Name,Major,Year,Email
U2310001A,Alice Tan,Computer Science,2,alice.tan@e.ntu.edu.sg
U2310002B,Bob Lee,Business Analytics,3,bob.lee@e.ntu.edu.sg
```

### staff_list.csv
```csv
StaffID,Name,Role,Department,Email
staff001,Dr. Sarah Chen,Director,Career Services,sarah.chen@ntu.edu.sg
```

### company_representative_list.csv
```csv
CompanyRepID,Name,CompanyName,Department,Position,Email,Status
jane.ong@techwave.com,Jane Ong,TechWave Pte Ltd,HR,HR Manager,jane.ong@techwave.com,Approved
```

## Running the System

### Compilation
```bash
javac -d bin src/code/**/*.java
```

### Execution
```bash
java -cp bin code.App
```

### Initial Login Credentials
All users have default password: `password`

**Students**: U2310001A, U2310002B, etc.
**Staff**: staff001, staff002, etc.
**Company Reps**: Use email addresses from CSV

## System Workflow Examples

### Student Workflow
1. Login with student ID
2. View available opportunities (filtered by major/year)
3. Apply for opportunities (max 3)
4. Check application status
5. Accept placement if successful
6. Request withdrawal if needed

### Company Rep Workflow
1. Register account (wait for approval)
2. Login after approval
3. Create internship opportunities
4. Wait for staff approval
5. Review student applications
6. Approve/reject applications
7. Toggle visibility ON/OFF

### Staff Workflow
1. Login with staff account
2. Approve company rep registrations
3. Approve internship opportunities
4. Review withdrawal requests
5. Generate system reports
6. Filter and analyze data

## Error Handling

All operations include:
- Input validation
- Business rule checking
- User-friendly error messages
- Automatic logging to `internship_system.log`
- Recovery suggestions

## Logging

System logs all operations:
- User login/logout
- Data operations
- Business transactions
- Errors and exceptions

Log file: `internship_system.log`

## Data Persistence

On exit, system saves:
- User data
- Opportunities
- Applications
- Requests
- All changes to CSV files

## Future Enhancements

Potential extensions:
1. Email notifications
2. Application deadlines with reminders
3. Interview scheduling
4. Performance evaluations
5. Analytics dashboard
6. Export to PDF/Excel
7. Batch operations
8. Advanced search
9. Audit trail
10. Multi-language support

## Credits

**Development Team**: Group 5
**Course**: SC/CE/CZ2002 - Object-Oriented Design & Programming
**Institution**: Nanyang Technological University
**Year**: 2025

---

**Total Implementation Statistics**
- **Total Files**: 50+
- **Lines of Code**: ~8,000+
- **Classes**: 40+
- **Interfaces**: 5+
- **Enums**: 5
- **Compilation Status**: ✅ All files compile without errors
