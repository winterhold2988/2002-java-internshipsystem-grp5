package code.model;

import code.enums.UserRole;

// Represents a company representative user.
public class CompanyRepresentative extends User {

    private String companyName;
    private String department;
    private String position;
    private boolean approved;

    public CompanyRepresentative(String id, String name, String password) {
        super(id, name, password, UserRole.COMPANY_REPRESENTATIVE);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
