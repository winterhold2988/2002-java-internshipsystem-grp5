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
