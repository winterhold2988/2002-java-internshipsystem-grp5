# Quick Start Guide - Internship Placement Management System

## 🚀 Installation & Running

### 1. Compile the Project
```bash
cd /Users/tmprithvi/Code/OOP/2002-java-internshipsystem-grp5
mkdir -p bin
javac -d bin -sourcepath src $(find src -name "*.java")
```

### 2. Run the Application
```bash
java -cp bin code.App
```

### 3. First Launch
The application will:
1. ✅ Initialize all repositories
2. ✅ Load data from CSV files in `src/resources/data/`
3. ✅ Generate unique IDs for new entities
4. ✅ Display welcome banner
5. ✅ Show data loading summary
6. ✅ Present login screen

## 📋 System Overview

This is a complete **Command-Line Interface (CLI)** application for managing internship placements at NTU.

### Three User Types

| User Type | ID Format | Example | Capabilities |
|-----------|-----------|---------|--------------|
| **Student** | U1234567A | U2310001A | View/apply for internships (max 3), accept placements, request withdrawals |
| **Company Rep** | Email | jane.ong@techwave.com | Create opportunities (max 5), review applications, approve/reject |
| **Staff** | NTU Account | sng001 | Approve reps, approve opportunities, handle withdrawals, generate reports |

**Default Password**: `password` (for all users)

**Students:**
- U2310001A - Tan Wei Ling (Computer Science, Year 2)
- U2310002B - Ng Jia Hao (Data Science & AI, Year 3)
- U2310003C - Lim Yi Xuan (Computer Engineering, Year 4)
- U2310004D - Chong Zhi Hao (Information Engineering & Media, Year 1)
- U2310005E - Wong Shu Hui (Computer Science, Year 3)

**Career Center Staff:**
- sng001 - Dr. Sng Hui Lin (CCDS)
- tan002 - Mr. Tan Boon Kiat (CCDS)
- lee003 - Ms. Lee Mei Ling (CCDS)

**Company Representatives:**
- jane.ong@techwave.com - Jane Ong (TechWave Pte Ltd - ✓ Approved)
- david.tan@innovables.com - David Tan (Innovables Ltd - ⏳ Pending)

## Working with Data

### Using the ID Generator
```java
AppConfig config = new AppConfig();
IDGenerator idGen = config.getIdGenerator();

// Generate unique IDs for new entities
String appId = idGen.generateApplicationId();        // APP0001
String oppId = idGen.generateOpportunityId();        // OPP0001
String reqId = idGen.generateRequestId();            // REQ0001
String wdrId = idGen.generateWithdrawalId();         // WDR0001
```

### Exporting Data
```java
DataPersistenceManager persist = config.getDataPersistenceManager();

// Export individual entity types
persist.exportStudents("./data/export/students.csv");
persist.exportStaff("./data/export/staff.csv");

// Export all data at once
persist.exportAll("./data/export/");
```

### Viewing Statistics
```java
DataStatistics stats = new DataStatistics(
    config.getUserRepository(),
    config.getInternshipRepository(),
    config.getApplicationRepository(),
    config.getRegistrationRequestRepository(),
    config.getWithdrawalRequestRepository()
);

// Print comprehensive report
stats.printReport();

// Get individual statistics
long studentCount = stats.countStudents();
Map<ApplicationStatus, Long> appStats = stats.countApplicationsByStatus();
```

### Data Validation
```java
// Validate user inputs before saving
if (DataValidator.isValidEmail(email)) {
    // Email is valid
}

if (DataValidator.isValidStudentId(studentId)) {
    // Student ID matches pattern U1234567A
}

if (DataValidator.isValidYearOfStudy(year)) {
    // Year is between 1 and 4
}
```

## Sample Login Credentials

All accounts use password: **password**

### Students
- **U2310001A** - Tan Wei Ling (Computer Science, Year 2)
- **U2310002B** - Ng Jia Hao (Data Science & AI, Year 3)
- **U2310003C** - Lim Yi Xuan (Computer Engineering, Year 4)

### Career Center Staff
- **sng001** - Dr. Sng Hui Lin
- **tan002** - Mr. Tan Boon Kiat
- **lee003** - Ms. Lee Mei Ling

### Company Representatives
- **jane.ong@techwave.com** - Jane Ong (✓ Approved)
- **david.tan@innovables.com** - David Tan (⏳ Pending Approval)

## Quick Test Scenarios

### Scenario 1: Student Applies for Internship

1. **Login as Student**
   ```
   User ID: U2310001A
   Password: password
   ```

2. **Navigate to Student Menu**
   - Choose option 1 (Access Student Menu)

3. **View Available Internships**
   - Choose option 1 (View Available Internships)
   - Note: Initially empty - staff must approve opportunities first

4. **Check Application Status**
   - Choose option 3 (View My Applications)

### Scenario 2: Company Creates Opportunity

1. **Login as Company Rep**
   ```
   User ID: jane.ong@techwave.com
   Password: password
   ```

2. **Navigate to Company Menu**
   - Choose option 1 (Access Company Representative Menu)

3. **Create Internship Opportunity**
   - Choose option 1 (Create Internship Opportunity)
   - Enter details:
     ```
     Title: Software Engineering Intern
     Description: Full-stack development position
     Preferred Major: Computer Science
     Level: 2 (INTERMEDIATE)
     Opening Date: 01/01/2025
     Closing Date: 31/03/2025
     Max Slots: 5
     ```

4. **View Created Opportunities**
   - Choose option 2 (View My Opportunities)
   - Status will be PENDING (needs staff approval)

### Scenario 3: Staff Approves Everything

1. **Login as Staff**
   ```
   User ID: sng001
   Password: password
   ```

2. **Navigate to Staff Menu**
   - Choose option 1 (Access Career Center Staff Menu)

3. **Approve Pending Company Rep**
   - Choose option 1 (Review Registration Requests)
   - Select david.tan@innovables.com (David Tan)
   - Choose option 1 (Approve)

4. **Approve Pending Opportunity**
   - Choose option 2 (Review Internship Opportunities)
   - Select the Software Engineering Intern opportunity
   - Choose option 1 (Approve)
   - This makes it visible to students

5. **View Reports**
   - Choose option 6 (Generate Reports)
   - Try different report types

### Scenario 4: Complete Application Workflow

1. **Company Creates Opportunity** (jane.ong@techwave.com)
   - Create internship as shown in Scenario 2

2. **Staff Approves** (sng001)
   - Approve the opportunity as shown in Scenario 3

3. **Student Applies** (U2310001A)
   - Login as U2310001A
   - View available internships (option 1)
   - Apply for internship (option 2)
   - Enter the Opportunity ID shown

4. **Company Reviews Application** (jane.ong@techwave.com)
   - Login back as jane.ong@techwave.com
   - View applications (option 4)
   - Review applications (option 5)
   - Approve the student's application

5. **Student Accepts Placement** (U2310001A)
   - Login back as U2310001A
   - Accept/Decline Placement (option 5)
   - Choose to accept

### Scenario 5: Withdrawal Request

1. **Student Applies** (U2310002B)
   - Apply for an internship (must be pending)

2. **Student Requests Withdrawal** (U2310002B)
   - Choose option 4 (Request Application Withdrawal)
   - Select the application
   - Enter reason: "Found another opportunity"

3. **Staff Reviews Withdrawal** (sng001)
   - Login as sng001
   - Choose option 5 (Review Withdrawal Requests)
   - Approve or reject the request

## Menu Navigation

### General Tips
- Enter the number corresponding to your choice
- Enter **0** to go back or logout
- All menus show options numbered from 1
- Press Enter when prompted to continue

### Student Menu Options
```
1. View Available Internships
2. Apply for Internship
3. View My Applications
4. Request Application Withdrawal
5. Accept/Decline Placement
6. Change Password
0. Back to Main Menu
```

### Company Representative Menu Options
```
1. Create Internship Opportunity
2. View My Opportunities
3. Edit Internship Opportunity
4. View Applications for My Opportunities
5. Review Applications (Approve/Reject)
6. Change Password
0. Back to Main Menu
```

### Career Center Staff Menu Options
```
1. Review Registration Requests
2. Review Internship Opportunities
3. View All Opportunities
4. View All Applications
5. Review Withdrawal Requests
6. Generate Reports
7. Manage Users
8. Change Password
0. Back to Main Menu
```

## Common Tasks

### Change Password
1. From Main Menu, choose option 2
2. Enter current password: password
3. Enter new password (cannot be "password")
4. Confirm new password

### Logout
1. From Main Menu, choose option 0
2. Confirm logout

### Exit Application
1. Logout first
2. At login screen, type "exit" as User ID

## Input Formats

### Dates
- Format: **dd/MM/yyyy**
- Example: 25/12/2025

### Yes/No Questions
- Accept: **y** or **yes**
- Decline: **n** or **no**

### Numeric Inputs
- Always enter whole numbers
- Stay within the valid range shown

## Troubleshooting

### "Account Not Approved" Message
- Company representatives must be approved by staff
- Login as sng001 and approve the registration

### "No Internships Available"
- Opportunities must be:
  1. Created by company rep
  2. Approved by staff
  3. Set to visible
  4. Have available slots
  5. Not past closing date

### "Invalid Password"
- Default password is "password" (all lowercase)
- If changed, use your new password
- Max 3 login attempts

### "You have already applied"
- Each student can only apply once per opportunity
- Check "View My Applications" for status

## Data Layer Files

The data package contains:
```
src/code/data/
├── CSVReader.java                      ← CSV parsing utility
├── StudentDataLoader.java              ← Loads students from CSV
├── StaffDataLoader.java                ← Loads staff from CSV
├── CompanyRepresentativeDataLoader.java ← Loads company reps from CSV
├── DataBootstrap.java                  ← Coordinates all data loading
├── DataPersistenceManager.java         ← Exports data to CSV
├── IDGenerator.java                    ← Generates unique IDs
├── DataValidator.java                  ← Validation utilities
└── DataStatistics.java                 ← Statistics and reporting
```

## Next Steps

After testing the data layer:
1. Implement service layer for business logic
2. Build CLI layer for user interaction
3. Add DTO classes for filtering and reporting
4. Create util classes for common operations
5. Write unit tests
6. Create comprehensive documentation

## Support

For issues or questions:
1. Check README.md for data layer documentation
2. Review individual class JavaDocs
3. Consult the assignment PDF for requirements

## Happy Coding! 🚀
