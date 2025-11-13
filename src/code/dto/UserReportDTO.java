package code.dto;

import code.enums.UserRole;
import code.model.*;

/**
 * Data transfer object representing summary information about a user.
 * Used for reports and display purposes.
 */
public class UserReportDTO {

    private final String id;
    private final String name;
    private final UserRole role;
    private final String roleSpecificInfo;
    private final String additionalInfo;

    private UserReportDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.role = builder.role;
        this.roleSpecificInfo = builder.roleSpecificInfo;
        this.additionalInfo = builder.additionalInfo;
    }

    /**
     * Creates a report DTO from a User entity.
     *
     * @param user the user to convert
     * @return a new UserReportDTO
     */
    public static UserReportDTO from(User user) {
        Builder builder = builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole());

        if (user instanceof Student) {
            Student student = (Student) user;
            builder.roleSpecificInfo(student.getMajor())
                    .additionalInfo("Year " + student.getYearOfStudy());
        } else if (user instanceof CareerCenterStaff) {
            CareerCenterStaff staff = (CareerCenterStaff) user;
            builder.roleSpecificInfo(staff.getDepartment())
                    .additionalInfo("Staff Member");
        } else if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            builder.roleSpecificInfo(rep.getCompanyName())
                    .additionalInfo(rep.getPosition() + " | " + (rep.isApproved() ? "✓ Approved" : "⏳ Pending"));
        }

        return builder.build();
    }

    /**
     * Formats this DTO as a readable string for display.
     *
     * @return a formatted string representation
     */
    public String toDisplayString() {
        return String.format("ID: %s | Name: %s | Role: %s\n%s | %s",
                id, name, role, roleSpecificInfo, additionalInfo);
    }

    /**
     * Formats this DTO as a single line summary.
     *
     * @return a one-line summary
     */
    public String toSummaryString() {
        return String.format("[%s] %s - %s (%s)",
                id, name, roleSpecificInfo, role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private UserRole role;
        private String roleSpecificInfo;
        private String additionalInfo;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder role(UserRole role) { this.role = role; return this; }
        public Builder roleSpecificInfo(String roleSpecificInfo) { this.roleSpecificInfo = roleSpecificInfo; return this; }
        public Builder additionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; return this; }

        public UserReportDTO build() {
            return new UserReportDTO(this);
        }
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public UserRole getRole() { return role; }
    public String getRoleSpecificInfo() { return roleSpecificInfo; }
    public String getAdditionalInfo() { return additionalInfo; }
}
