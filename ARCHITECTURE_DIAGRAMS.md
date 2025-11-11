# CLI Architecture & Flow Diagrams

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         App.java (Main Entry)                    │
│  • Initializes 5 repositories                                   │
│  • Bootstraps sample data                                       │
│  • Displays welcome banner                                      │
│  • Main application loop                                        │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                      LoginHandler                                │
│  • Authenticates user (max 3 attempts)                          │
│  • Validates credentials                                        │
│  • Warns about default password                                 │
│  • Returns authenticated User or null                           │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                        MainMenu                                  │
│  • Routes to role-specific menu                                 │
│  • Provides logout functionality                                │
│  • Handles password changes                                     │
└──────────┬──────────────────┬──────────────────┬────────────────┘
           │                  │                  │
           ▼                  ▼                  ▼
    ┌─────────────┐   ┌──────────────┐   ┌─────────────────┐
    │StudentMenu  │   │CompanyRep    │   │CareerCenter     │
    │             │   │Menu          │   │StaffMenu        │
    └─────────────┘   └──────────────┘   └─────────────────┘
           │                  │                  │
           └──────────────────┴──────────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │   MenuBase     │
                    │  (Abstract)    │
                    └────────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │   CLIUtil      │
                    │  (Utilities)   │
                    └────────────────┘
```

## Class Hierarchy

```
MenuBase (Abstract)
    │
    ├── MainMenu
    │
    ├── StudentMenu
    │
    ├── CompanyRepresentativeMenu
    │
    └── CareerCenterStaffMenu

CLIUtil (Utility - Standalone)

LoginHandler (Standalone)
```

## Data Flow Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                         Repositories                              │
│  ┌────────────────┐  ┌──────────────────┐  ┌─────────────────┐ │
│  │UserRepository  │  │InternshipRepo    │  │ApplicationRepo  │ │
│  └────────────────┘  └──────────────────┘  └─────────────────┘ │
│  ┌─────────────────────────┐  ┌──────────────────────────────┐ │
│  │RegistrationRequestRepo  │  │WithdrawalRequestRepo         │ │
│  └─────────────────────────┘  └──────────────────────────────┘ │
└──────────────────┬───────────────────────────────────────────────┘
                   │
                   │ Passed via constructor
                   │
                   ▼
           ┌───────────────┐
           │  MenuBase     │
           │  (stores refs)│
           └───────┬───────┘
                   │
        ┌──────────┼──────────┐
        │          │          │
        ▼          ▼          ▼
   Student    Company    Staff
    Menu        Menu      Menu
        │          │          │
        └──────────┼──────────┘
                   │
                   │ CRUD operations
                   │
                   ▼
            Repositories
```

## User Flow - Student

```
┌─────────┐
│ Login   │
│ (S001)  │
└────┬────┘
     │
     ▼
┌─────────────┐
│ Main Menu   │
│ 1. Student  │  ◄── Choose option 1
│ 2. Password │
│ 0. Logout   │
└────┬────────┘
     │
     ▼
┌──────────────────────────────┐
│ Student Menu                 │
│ 1. View Internships          │ ◄─┐
│ 2. Apply                     │   │ Option 1
│ 3. View My Applications      │   │
│ 4. Request Withdrawal        │   │
│ 5. Accept/Decline Placement  │   │
│ 6. Change Password           │   │
│ 0. Back                      │   │
└────┬─────────────────────────┘   │
     │                             │
     ▼                             │
┌──────────────────────────────┐   │
│ View Available Internships    │──┘
│ • Filter by approved         │
│ • Filter by visible          │
│ • Filter by not expired      │
│ • Filter by slots available  │
│ • Display list               │
└──────────────────────────────┘
```

## User Flow - Company Representative

```
┌─────────┐
│ Login   │
│ (CR001) │
└────┬────┘
     │
     ▼
┌──────────────────┐
│ Approval Check   │
│ isApproved()?    │
└────┬─────────────┘
     │
     ├─ No ──► Display "Pending Approval" → Exit
     │
     └─ Yes
         │
         ▼
┌──────────────────────────────┐
│ Company Representative Menu   │
│ 1. Create Opportunity        │ ◄─┐
│ 2. View My Opportunities     │   │ Option 1
│ 3. Edit Opportunity          │   │
│ 4. View Applications         │   │
│ 5. Review Applications       │   │
│ 6. Change Password           │   │
│ 0. Back                      │   │
└────┬─────────────────────────┘   │
     │                             │
     ▼                             │
┌──────────────────────────────┐   │
│ Create Internship            │───┘
│ • Input: title, desc, etc    │
│ • Set status = PENDING       │
│ • Set visible = false        │
│ • Save to repository         │
│ • Awaits staff approval      │
└──────────────────────────────┘
```

## User Flow - Career Center Staff

```
┌─────────┐
│ Login   │
│(STAFF001│
└────┬────┘
     │
     ▼
┌──────────────────────────────┐
│ Career Center Staff Menu      │
│ 1. Review Registrations      │ ◄─┐
│ 2. Review Opportunities      │   │ Option 1
│ 3. View All Opportunities    │   │
│ 4. View All Applications     │   │
│ 5. Review Withdrawals        │   │
│ 6. Generate Reports          │   │
│ 7. Manage Users              │   │
│ 8. Change Password           │   │
│ 0. Back                      │   │
└────┬─────────────────────────┘   │
     │                             │
     ▼                             │
┌──────────────────────────────┐   │
│ Review Registration Requests  │──┘
│ • List pending requests      │
│ • Select request             │
│ • View representative details│
│ • Approve or Reject          │
│   - Sets rep.approved = true │
│   - Enables account access   │
└──────────────────────────────┘
```

## State Transitions

### Internship Opportunity States

```
  ┌─────────┐
  │ PENDING │ ◄── Created by Company Rep
  └────┬────┘
       │
       ├─ Staff Approves ──► ┌──────────┐
       │                     │ APPROVED │
       │                     └────┬─────┘
       │                          │
       │                          ├─ All slots filled ──► ┌────────┐
       │                          │                        │ FILLED │
       │                          │                        └────────┘
       │                          └─ Slots available ──► (stays APPROVED)
       │
       └─ Staff Rejects ───► ┌──────────┐
                             │ REJECTED │
                             └──────────┘
```

### Application States

```
  ┌─────────┐
  │ PENDING │ ◄── Student applies
  └────┬────┘
       │
       ├─ Company Approves ──► ┌────────────┐
       │                       │ SUCCESSFUL │
       │                       └────────────┘
       │
       ├─ Company Rejects ───► ┌──────────────┐
       │                       │ UNSUCCESSFUL │
       │                       └──────────────┘
       │
       └─ Student withdraws ──► ┌──────────────────────┐
                                │ WITHDRAWAL_REQUESTED │
                                └──────────┬───────────┘
                                           │
                                           ├─ Staff Approves ──► UNSUCCESSFUL
                                           │
                                           └─ Staff Rejects ───► PENDING
```

### Company Representative Registration

```
  ┌────────────────┐
  │ Registration   │
  │ Created        │
  │ (rep.approved  │
  │  = false)      │
  └───────┬────────┘
          │
          ├─ Staff Approves ──► ┌──────────────┐
          │                     │ rep.approved │
          │                     │ = true       │
          │                     └──────────────┘
          │                           │
          │                           └─► Can access system
          │
          └─ Staff Rejects ────► ┌──────────────┐
                                 │ rep.approved │
                                 │ = false      │
                                 └──────────────┘
                                       │
                                       └─► Cannot access system
```

## Interaction Sequence - Complete Application Flow

```
Student (S001)         Company (CR001)      Staff (STAFF001)
    │                        │                    │
    │ 1. Login              │                    │
    ├──────────────────────►│                    │
    │                        │                    │
    │                        │ 2. Login          │
    │                        ├───────────────────►│
    │                        │                    │
    │                        │ 3. Create Opp     │
    │                        │ (PENDING)         │
    │                        │◄──────────────────┤
    │                        │                    │
    │                        │                    │ 4. Approve Opp
    │                        │                    │ (APPROVED)
    │                        │◄───────────────────┤
    │                        │                    │
    │ 5. View Internships   │                    │
    │ (sees approved opp)   │                    │
    ├──────────────────────►│                    │
    │                        │                    │
    │ 6. Apply              │                    │
    │ (status: PENDING)     │                    │
    ├──────────────────────►│                    │
    │                        │                    │
    │                        │ 7. Review App     │
    │                        │ Approve           │
    │                        │ (SUCCESSFUL)      │
    │◄───────────────────────┤                    │
    │                        │                    │
    │ 8. Accept Placement   │                    │
    │ (placement_accepted   │                    │
    │  = true)              │                    │
    └───────────────────────┘                    │
                                                  │
```

## Menu State Machine

```
                    ┌──────────────┐
                    │ Login Screen │
                    └──────┬───────┘
                           │
                    Success│
                           ▼
                    ┌──────────────┐
              ┌────►│  Main Menu   │◄────┐
              │     └──────┬───────┘     │
              │            │              │
              │     Choose │ Role Menu    │
              │            ▼              │
              │     ┌──────────────┐     │
              │     │  Role Menu   │     │
              │     │ (Student/    │     │
              │     │  Company/    │     │
              │     │  Staff)      │     │
              │     └──────┬───────┘     │
              │            │              │
              │     Choose │ 0 (Back)     │
              │     action │              │
              │            ▼              │
              │     ┌──────────────┐     │
              │     │  Execute     │     │
              │     │  Action      │     │
              │     └──────┬───────┘     │
              │            │              │
              └────────────┘              │
                                          │
              Choose 0 (Logout)──────────┘
                      │
                      ▼
              ┌──────────────┐
              │ Confirm      │
              │ Logout?      │
              └──────┬───────┘
                     │
              Yes    │
                     ▼
              ┌──────────────┐
              │ Return to    │
              │ Login Screen │
              └──────────────┘
```

## Repository Access Pattern

```
Menu Methods
     │
     │ needs data
     │
     ▼
Repository
     │
     │ query/modify
     │
     ▼
In-Memory Storage (Map/List)
     │
     │ return data
     │
     ▼
Menu Methods
     │
     │ display/process
     │
     ▼
User Interface
```

## Summary

This CLI architecture provides:
- **Clean Separation**: UI logic separate from business logic
- **Role-Based Access**: Different menus for different user types
- **State Management**: Clear state transitions for all entities
- **Modularity**: Easy to extend with new features
- **Maintainability**: Well-organized, documented code
- **User-Friendly**: Intuitive navigation and clear feedback
