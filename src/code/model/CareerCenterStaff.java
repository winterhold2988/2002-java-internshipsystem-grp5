package code.model;

import code.enums.UserRole;

// Represents a staff member of the Career Center.
public class CareerCenterStaff extends User {

    private String department;

    public CareerCenterStaff(String id, String name, String password, String department) {
        super(id, name, password, UserRole.CAREER_CENTER_STAFF);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
