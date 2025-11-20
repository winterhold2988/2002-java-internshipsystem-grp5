# Internship Placement Management System
**SC2002 - Object-Oriented Design & Programming**

---

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Project Architecture](#project-architecture)
4. [Package Explanations](#package-explanations)
5. [UML Diagrams](#uml-diagrams)
6. [Getting Started](#getting-started)

---

## Overview

The **Internship Placement Management System** is a centralized platform designed to streamline the internship application process for three distinct user types:

- **Students** 
- **Company Representatives** 
- **Career Center Staff** 

The system provides role-based access control, CSV data import/export capabilities, and comprehensive reporting features. All user data is loaded from CSV files at startup, and changes are persisted back to CSV files on exit. For more information refer to the README.md file.

### Key Objectives
- Facilitate seamless internship opportunity discovery and application
- Enable efficient review and approval workflows
- Provide comprehensive tracking and reporting capabilities
- Maintain data integrity through validation and business rule enforcement

---

## Features

### Core System Capabilities

#### **Multi-User Role Support**
- Three distinct user roles with specialized interfaces and permissions
- Role-based menu navigation with secure authentication
- Different ID formats for each user type

#### **Student Features**
- View available internships filtered by major and year of study
- Apply for up to 3 concurrent internships
- Track application status in real-time
- Accept/decline placement offers
- Request withdrawal of pending AND successful applications
- Level-based opportunity filtering (Year 1-2: BASIC only; Year 3-4: All levels)
- Major-based filtering (students only see opportunities matching their major or open to all majors)
- Configurable filters: Status, Major, Level, Closing Date
- Persistent filter settings across menu navigation
- Alphabetical sorting by default

#### **Company Representative Features**
- Self-registration with staff approval workflow
- Create up to 5 internship opportunities
- Edit opportunity details (title, description, dates, slots)
- Review and approve/reject student applications
- Toggle opportunity visibility for students
- View application statistics
- Configurable filters for viewing opportunities
- Filter settings persist across menu navigation

#### **Career Center Staff Features**
- Approve/reject company representative registrations
- Approve/reject internship opportunities before visibility
- Review and process withdrawal requests
- Generate comprehensive system reports
- Configurable filters: Status, Major, Level, Closing Date
- Filter settings persist across menu navigation
- Manage users across all roles

#### **Data Management**
- CSV-based data import on startup
- Automatic data persistence on exit
- Comprehensive logging system (`internship_system.log`)
- Data validation and integrity checks
- Statistics and reporting capabilities

#### **Application Workflow**
- Application lifecycle: PENDING → SUCCESSFUL/UNSUCCESSFUL
- Automatic slot management and availability tracking
- Withdrawal request system with staff approval
- One-placement-per-student rule enforcement
- Automatic withdrawal of other applications upon placement acceptance

#### **Reporting & Analytics**
- System-wide statistics (users, opportunities, applications)
- Company activity reports
- Student activity reports
- Customizable filters (status, major, level, closing date)
- Real-time slot availability tracking

---

## Project Architecture

The project follows a **layered architecture** with clear separation of concerns:

```
src/
  code/
    App.java                     # Application entry point
    │
    ├── cli/                     # Presentation Layer
    │                            # User interface menus and input handling
    │
    ├── config/                  # Configuration Layer
    │                            # Dependency injection and system configuration
    │
    ├── data/                    # Data Access Layer
    │                            # CSV import/export, validation, and persistence
    │
    ├── dto/                     # Data Transfer Objects
    │                            # Report generation and filtering
    │
    ├── enums/                   # Type-Safe Constants
    │                            # User roles, statuses, and levels
    │
    ├── model/                   # Domain Layer
    │                            # Business entities and domain logic
    │
    ├── repository/              # Repository Layer
    │                            # Data storage abstraction
    │
    ├── service/                 # Business Logic Layer
    │                            # Complex workflows and business rules
    │
    └── util/                    # Utility Layer
        │                        # Logging, validation, formatting
        └── exception/           # Custom exception hierarchy

  resources/
    └── data/                    # CSV Data Files
                                 # Student, staff, and company data
```

### Design Principles & Patterns

**SOLID Principles:**
- Single Responsibility Principle
- Open/Closed Principle
- Liskov Substitution Principle
- Interface Segregation Principle
- Dependency Inversion Principle

**Design Patterns:**
- Factory Pattern (Menu creation)
- Repository Pattern (Data access)
- Template Method Pattern (Menu base class)
- Builder Pattern (Filter construction)
- Dependency Injection (Configuration)

---

## Package Explanations

### **cli** - Command Line Interface Package
**Purpose**: Handles all user interactions through console-based menus. Provides role-specific interfaces for Students, Company Representatives, and Career Center Staff with shared common functionality.

**OO/SOLID Principles Applied**:
- **Dependency Inversion Principle (DIP)**: `StudentMenu.java` depends on `StudentApplicationService` abstraction rather than concrete repositories
- **Open/Closed Principle (OCP)**: `RoleMenuFactory.java` and `DefaultRoleMenuFactory.java` allow extending menu creation without modifying `MainMenu.java`
- **Template Method Pattern**: `MenuBase.java` provides abstract `display()` method and common functionality inherited by all concrete menu classes

---

### **config** - Configuration Package
**Purpose**: Centralized application configuration and dependency injection container. Initializes all repositories, services, and data management components at startup.

**OO/SOLID Principles Applied**:
- **Dependency Injection**: `AppConfig.java` creates and wires all dependencies, providing single point of configuration
- **Single Responsibility Principle (SRP)**: Responsible only for application-wide configuration and initialization

---

### **data** - Data Management Package
**Purpose**: Manages CSV data import/export, validation, ID generation, and bootstrap initialization. Handles all file I/O operations and data persistence.

**OO/SOLID Principles Applied**:
- **Single Responsibility Principle (SRP)**: Each loader class (`StudentDataLoader.java`, `StaffDataLoader.java`, `CompanyRepresentativeDataLoader.java`) handles loading for one specific entity type
- **Separation of Concerns**: `CSVReader.java` handles parsing, `DataValidator.java` handles validation, `DataPersistenceManager.java` handles export, `DataBootstrap.java` coordinates

---

### **dto** - Data Transfer Objects Package
**Purpose**: Provides data transfer objects for reporting and filtering without exposing internal domain models. Uses Builder Pattern for flexible filter construction.

**OO/SOLID Principles Applied**:
- **Builder Pattern**: `InternshipOpportunityFilter.java`, `InternshipApplicationFilter.java`, `UserFilter.java` use builder pattern for flexible, readable filter construction
- **Encapsulation**: DTOs (`OpportunityReportDTO.java`, `ApplicationReportDTO.java`, `UserReportDTO.java`) provide read-only views of domain data
- **Factory Pattern**: `ReportGenerator.java` acts as factory for creating various report types from repository data

---

### **enums** - Enumerations Package
**Purpose**: Defines fixed sets of type-safe constants used throughout the system. Includes user roles, application statuses, opportunity statuses, internship levels, and withdrawal decisions.

**OO/SOLID Principles Applied**:
- **Type Safety**: Enums (`UserRole.java`, `ApplicationStatus.java`, `OpportunityStatus.java`, etc.) prevent invalid values and provide compile-time checking
- **Single Source of Truth**: Centralized definition of system constants prevents duplication and ensures consistency

---

### **model** - Domain Model Package
**Purpose**: Core business entities representing domain concepts. Contains rich domain models with encapsulated business logic and validation rules.

**OO/SOLID Principles Applied**:
- **Inheritance & Polymorphism**: `User.java` abstract base class extended by `Student.java`, `CompanyRepresentative.java`, `CareerCenterStaff.java`
- **Encapsulation**: `InternshipOpportunity.java` encapsulates business logic in `isAvailable()` method, hiding complex availability rules
- **Rich Domain Model**: Entities contain both data and behavior relevant to their domain concept

---

### **repository** - Repository Pattern Package
**Purpose**: Abstracts data storage and retrieval operations using Repository Pattern. Provides in-memory storage with clean interfaces for data access.

**OO/SOLID Principles Applied**:
- **Repository Pattern**: All repositories (`UserRepository.java`, `InternshipRepository.java`, `ApplicationRepository.java`) provide consistent interface for data access
- **Abstraction**: Hides implementation details (currently `LinkedHashMap`) from business logic, making storage mechanism easily swappable
- **Single Responsibility Principle (SRP)**: Each repository manages one entity type only

---

### **filter** - Filter Management Package
**Purpose**: Provides comprehensive, persistent filtering system for internship opportunities with user-configurable criteria.

**Key Classes**:
- **OpportunityFilterCriteria.java**: Immutable value object holding filter settings (Status, Major, Level, Closing Date)
- **OpportunityFilterService.java**: Stateless service that applies filters and sorts opportunities alphabetically
- **UserFilterState.java**: Stores per-user filter preferences with default alphabetical sorting
- **FilterStateManager.java**: Thread-safe manager for all users' filter states (session-based persistence)

**OO/SOLID Principles Applied**:
- **Single Responsibility Principle (SRP)**: Each class handles one aspect (criteria, service, state, manager)
- **Open/Closed Principle (OCP)**: Extensible via Builder pattern and new filter methods without modifying existing code
- **Dependency Inversion Principle (DIP)**: FilterStateManager abstracts persistence (could be extended to database)
- **Immutability Pattern**: OpportunityFilterCriteria is thread-safe with final fields and no setters
- **Builder Pattern**: Flexible construction of filter criteria

---

### **service** - Business Logic Package
**Purpose**: Encapsulates complex business rules and workflows. Acts as intermediary between presentation layer (CLI) and data layer (repositories). Includes year-based and major-based filtering logic for internship opportunities.

**Key Services**:
- **InternshipService.java**: Filters internships by year (Year 1-2: BASIC only; Year 3-4: All levels) and major (case-insensitive matching)
- **StudentApplicationService.java**: Interface for student application operations including withdrawal requests
- **DefaultStudentApplicationService.java**: Implements student operations; supports withdrawal of both pending and successful applications
- **LoginService.java**: Handles user authentication

**OO/SOLID Principles Applied**:
- **Dependency Inversion Principle (DIP)**: `StudentApplicationService.java` interface allows CLI to depend on abstraction rather than concrete implementation
- **Single Responsibility Principle (SRP)**: `DefaultStudentApplicationService.java` coordinates repositories for student-related actions only
- **Interface Segregation**: Service interfaces expose only relevant methods needed by CLI layer

---

### **util** - Utility Package
**Purpose**: Reusable helper classes for logging, console formatting, input validation, date/time operations, and error handling. Includes custom exception hierarchy.

**OO/SOLID Principles Applied**:
- **Single Responsibility Principle (SRP)**: Each utility class has one focused purpose (`Logger.java` for logging, `InputValidator.java` for validation, `DateTimeUtil.java` for dates)
- **Exception Hierarchy**: `InternshipSystemException.java` serves as base with specialized subclasses (`ValidationException.java`, `BusinessRuleException.java`, `DataNotFoundException.java`)
- **Static Utility Methods**: Utility classes provide static methods for common operations without requiring instantiation

---

## UML Diagrams

The Class and Sequence diagrams for this project can be found in the `uml/` directory. It includes a comprehensive UML class diagram covering all major classes, as well as a UML sequence diagram to illustrate the interaction flow of objects. 

---

## Getting Started

### Prerequisites

Ensure the following requirements are met before running the application:

- **Java Development Kit (JDK)**: Version 11 or higher
- **Operating System**: Windows, macOS, or Linux
- **Terminal/Command Prompt**: For running the application
- **CSV Data Files**: Ensure all CSV files are present in `src/resources/data/`

### Installation

1. **Verify CSV Data Files**
   - Ensure the following files exist in `src/resources/data/`:
     - `student_list.csv`
     - `staff_list.csv`
     - `company_representative_list.csv`

2. **Compile the Project**
   ```bash
   # Create bin directory if it doesn't exist
   mkdir -p bin

   # Compile all Java files
   javac -d bin $(find src/code -name "*.java")
   ```

### Running the Application

1. **Start the Application**
   ```bash
   java -cp bin code.App
   ```

2. **Login with Sample Credentials**

   **Students**:
   - User ID: `U2310001A` | Password: `password`
   - User ID: `U2310002A` | Password: `password`

   **Company Representatives**:
   - User ID: `jane.ong@techwave.com` | Password: `password` (Approved)
   - User ID: `mark.lee@innovatecorp.com` | Password: `password` (Approved)

   **Career Center Staff**:
   - User ID: `sng001` | Password: `password`
   - User ID: `lim002` | Password: `password`

3. **Navigate the Menu System**
   - Enter the number corresponding to your desired action
   - Follow the on-screen prompts
   - Enter `0` to go back or logout

4. **Exit the Application**
   - Type `exit` at the login prompt to quit
   - All data will be automatically saved to CSV files

### Quick Test Scenarios

#### **Scenario 1: Student Applies for Internship**
1. Login as student (`U2310001A`)
2. Select "Access Student Menu" (Option 1)
3. Select "View Available Internships" (Option 1)
4. Select "Apply for Internship" (Option 2)
5. Enter the internship ID to apply

#### **Scenario 2: Company Creates Opportunity**
1. Login as company rep (`jane.ong@techwave.com`)
2. Select "Access Company Menu" (Option 2)
3. Select "Create Internship Opportunity" (Option 1)
4. Fill in opportunity details

#### **Scenario 3: Staff Approves Opportunity**
1. Login as staff (`sng001`)
2. Select "Access Staff Menu" (Option 3)
3. Select "Review Internship Opportunities" (Option 2)
4. Approve pending opportunities

### Logging

All system activities are logged to `internship_system.log` in the root directory. Check this file for:
- User login/logout events
- Application submissions
- Approval/rejection actions
- Error messages and stack traces

---

**System developed by Group 5**  
**SC/CE/CZ2002 - Object-Oriented Design & Programming**  
**Nanyang Technological University - 2025**