# Quick Start Guide - Internship Placement Management System

## Installation & Running

### 1. Compile the Project
```bash
cd /Users/tmprithvi/Code/OOP/2002-java-internshipsystem-grp5
mkdir -p bin
javac -d bin $(find src/main/java -name "*.java")
```

### 2. Run the Application
```bash
java -cp bin edu.ntu.ccds.sc2002.internship.App
```

## Sample Login Credentials

All accounts use password: **password**

### Students
- **S001** - Alice Tan (Computer Science, Year 2)
- **S002** - Bob Lee (Business Analytics, Year 3)
- **S003** - Charlie Wong (Data Science, Year 1)

### Career Center Staff
- **STAFF001** - Dr. Sarah Chen (Career Services)
- **STAFF002** - Mr. David Lim (Student Affairs)

### Company Representatives
- **CR001** - John Smith (TechCorp - ✓ Approved)
- **CR002** - Mary Johnson (InnovateLabs - ✓ Approved)
- **CR003** - Peter Tan (StartupHub - ⏳ Pending Approval)

## Quick Test Scenarios

### Scenario 1: Student Applies for Internship

1. **Login as Student**
   ```
   User ID: S001
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
   User ID: CR001
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
   User ID: STAFF001
   Password: password
   ```

2. **Navigate to Staff Menu**
   - Choose option 1 (Access Career Center Staff Menu)

3. **Approve Pending Company Rep**
   - Choose option 1 (Review Registration Requests)
   - Select CR003 (Peter Tan)
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

1. **Company Creates Opportunity** (CR001)
   - Create internship as shown in Scenario 2

2. **Staff Approves** (STAFF001)
   - Approve the opportunity as shown in Scenario 3

3. **Student Applies** (S001)
   - Login as S001
   - View available internships (option 1)
   - Apply for internship (option 2)
   - Enter the Opportunity ID shown

4. **Company Reviews Application** (CR001)
   - Login back as CR001
   - View applications (option 4)
   - Review applications (option 5)
   - Approve the student's application

5. **Student Accepts Placement** (S001)
   - Login back as S001
   - Accept/Decline Placement (option 5)
   - Choose to accept

### Scenario 5: Withdrawal Request

1. **Student Applies** (S002)
   - Apply for an internship (must be pending)

2. **Student Requests Withdrawal** (S002)
   - Choose option 4 (Request Application Withdrawal)
   - Select the application
   - Enter reason: "Found another opportunity"

3. **Staff Reviews Withdrawal** (STAFF001)
   - Login as STAFF001
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
- Login as STAFF001 and approve the registration

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

## Files Created

```
src/main/java/edu/ntu/ccds/sc2002/internship/
├── cli/
│   ├── CLIUtil.java                    ← Utility methods
│   ├── LoginHandler.java               ← Login system
│   ├── MenuBase.java                   ← Base menu class
│   ├── MainMenu.java                   ← Main menu
│   ├── StudentMenu.java                ← Student features
│   ├── CompanyRepresentativeMenu.java  ← Company features
│   ├── CareerCenterStaffMenu.java      ← Staff features
│   └── README.md                       ← CLI documentation
├── App.java                            ← Updated entry point
└── ...other existing folders...
```

## Next Steps

After testing the CLI:
1. Implement service layer for business logic
2. Add data loaders for CSV files
3. Implement additional features
4. Write unit tests
5. Create user documentation

## Support

For issues or questions:
1. Check CLI_IMPLEMENTATION_SUMMARY.md for details
2. Review cli/README.md for technical documentation
3. Consult the assignment PDF for requirements

## Happy Testing! 🚀
