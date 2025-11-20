# CLI Package Documentation

## Overview

The `cli` package provides the command-line interface orchestration layer for the Internship Placement Management System. It handles user authentication, menu navigation, and role-specific functionality.

## Package Structure

```
cli/
├── CLIUtil.java                       # Utility methods for CLI operations
├── LoginHandler.java                  # Authentication and login
├── MenuBase.java                      # Base class for all menus
├── MainMenu.java                      # Main menu dispatcher
├── StudentMenu.java                   # Student-specific functionality
├── CompanyRepresentativeMenu.java     # Company representative functionality
└── CareerCenterStaffMenu.java         # Staff administrative functionality
```

## Components

### CLIUtil.java
Provides common CLI utilities:
- Input reading and validation (strings, integers, dates)
- User confirmation prompts (yes/no)
- Display formatting (headers, separators, messages)
- Date formatting utilities
- Scanner management

**Key Methods:**
- `readString(prompt)` - Read non-empty string
- `readInt(prompt, min, max)` - Read integer with range validation
- `readDate(prompt)` - Read date in dd/MM/yyyy format
- `readYesNo(prompt)` - Read yes/no confirmation
- `displaySuccess/Error/Info(message)` - Display formatted messages
- `printHeader(title)` - Print section header
- `pause()` - Wait for user to press Enter

### LoginHandler.java
Manages user authentication and company representative self-registration:
- Validates user credentials
- Supports up to 3 login attempts
- Detects default password usage
- Allows user to exit during login
- Enables company representatives to self-register (requires staff approval)

**Features:**
- Login attempt limiting (3 attempts)
- Default password warning
- User-friendly error messages
- Exit option during login
- Self-registration for company representatives (type 'register' at login prompt)
- Email validation and password confirmation for new registrations

### MenuBase.java
Abstract base class for all menu implementations:
- Provides access to all repositories
- Stores current user context
- Implements common functionality (password change)
- Defines menu structure

**Common Features:**
- Change password (with validation)
- Access to all repositories
- Current user information

### MainMenu.java
Main menu that routes users to role-specific menus:
- Dispatches to appropriate role menu (Student/Company/Staff)
- Provides logout functionality
- Handles password changes

**Menu Options:**
1. Access Role-Specific Menu
2. Change Password
0. Logout

### StudentMenu.java
Student-specific functionality:

**Menu Options:**
1. View Available Internships
2. Apply for Internship
3. View My Applications
4. Request Application Withdrawal
5. Accept/Decline Placement
6. Change Password
0. Back to Main Menu

**Features:**
- Browse approved, visible internships filtered by student's year and major
- Submit internship applications
- Track application status
- Request withdrawal for pending AND successful applications
- Accept or decline successful placements
- Automatic filtering: Year 1-2 see BASIC only; Year 3-4 see all levels
- Major matching: Only see opportunities for student's major or open to all majors
- Configure custom filters: Status, Major, Level, Closing Date
- Clear filters to reset to default
- Filter settings persist across menu navigation

### CompanyRepresentativeMenu.java
Company representative functionality:

**Menu Options:**
1. Create Internship Opportunity
2. View My Opportunities
3. Edit Internship Opportunity
4. View Applications for My Opportunities
5. Review Applications (Approve/Reject)
6. Change Password
0. Back to Main Menu

**Features:**
- Create new internship opportunities (pending staff approval)
- Edit opportunity details (title, description, dates, slots, visibility)
- View all applications received
- Approve/reject student applications
- Automatic slot management
- Approval requirement check (only approved reps can access)

### CareerCenterStaffMenu.java
Career center staff administrative functionality:

**Menu Options:**
1. Review Registration Requests
2. Review Internship Opportunities
3. View All Opportunities
4. View All Applications
5. Review Withdrawal Requests
6. Generate Reports
7. Manage Users
8. Change Password
0. Back to Main Menu

**Features:**
- Approve/reject company representative registrations
- Approve/reject internship opportunities
- View all system data
- Handle withdrawal requests (with slot management)
- Generate statistical reports:
  - Registration statistics
  - Opportunity statistics
  - Application statistics
  - Placement statistics
- User management by role

## User Flows

### Student Flow
1. Login → Main Menu → Student Menu
2. Browse available internships
3. Submit applications
4. Track application status
5. Accept/decline placements

### Company Representative Flow
1. Login → Main Menu → Company Menu
2. Check approval status
3. Create internship opportunities
4. Edit opportunities (title, description, dates, slots)
5. Review and approve/reject applications
6. Monitor slot availability

### Career Center Staff Flow
1. Login → Main Menu → Staff Menu
2. Review and approve company registrations
3. Review and approve internship opportunities
4. Monitor all applications
5. Handle withdrawal requests
6. Generate reports
7. Manage users

## Data Flow

```
App.java
  ├── Initializes repositories
  ├── Bootstraps sample data
  └── Main loop:
      ├── LoginHandler.login() → User
      └── MainMenu.display()
          └── Role-specific menu based on User.getRole()
              ├── StudentMenu.display()
              ├── CompanyRepresentativeMenu.display()
              └── CareerCenterStaffMenu.display()
```

## Input Validation

All inputs are validated:
- **Non-empty strings**: Required fields cannot be empty
- **Integer ranges**: Menu choices validated against available options
- **Dates**: Must be in dd/MM/yyyy format
- **Positive integers**: Slots and similar values must be positive
- **Password**: Cannot set password to default "password"

## Error Handling

- Invalid menu choices: Re-prompt with error message
- Login failures: Track attempts (max 3)
- Data not found: Display error and return to menu
- Permission checks: Verify user can perform action
- Slot management: Prevent over-allocation

## Sample Data

The system bootstraps with sample data:

**Students:**
- S001: Alice Tan (Computer Science, Year 2)
- S002: Bob Lee (Business Analytics, Year 3)
- S003: Charlie Wong (Data Science, Year 1)

**Staff:**
- STAFF001: Dr. Sarah Chen (Career Services)
- STAFF002: Mr. David Lim (Student Affairs)

**Company Representatives:**
- CR001: John Smith (TechCorp - Approved)
- CR002: Mary Johnson (InnovateLabs - Approved)
- CR003: Peter Tan (StartupHub - Pending)

**Default Password:** `password` (for all users)

## Running the Application

```bash
# Compile
javac src/main/java/edu/ntu/ccds/sc2002/internship/App.java

# Run
java -cp src/main/java edu.ntu.ccds.sc2002.internship.App
```

## Design Patterns

1. **Template Method**: MenuBase provides template for all menus
2. **Strategy Pattern**: Different menu implementations for different roles
3. **Singleton-like**: CLIUtil manages single Scanner instance
4. **Repository Pattern**: Data access abstraction

## Security Features

- Password masking (basic - displays during input)
- Default password detection and warning
- Login attempt limiting
- Permission checks (e.g., only approved reps can create opportunities)

## Future Enhancements

Potential improvements:
- Password masking using Console API
- Session timeout
- Audit logging
- Advanced search and filtering
- Email notifications
- File export for reports
- Batch operations
