package code.dto;

import code.enums.UserRole;
import code.model.*;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Filter criteria for searching and filtering users.
 * Uses the builder pattern for flexible filter construction.
 */
public class UserFilter {

    private UserRole role;
    private String nameContains;
    private Boolean approved; // For company representatives
    private String department; // For staff
    private String major; // For students
    private Integer yearOfStudy; // For students
    private String companyName; // For company representatives

    private UserFilter() {
        // Private constructor - use builder
    }

    /**
     * Applies this filter to a collection of users.
     *
     * @param users the users to filter
     * @return a stream of filtered users
     */
    public Stream<User> apply(Collection<User> users) {
        Stream<User> stream = users.stream();

        if (role != null) {
            stream = stream.filter(user -> user.getRole() == role);
        }

        if (nameContains != null) {
            String lowerSearch = nameContains.toLowerCase();
            stream = stream.filter(user -> user.getName() != null && 
                    user.getName().toLowerCase().contains(lowerSearch));
        }

        if (approved != null) {
            stream = stream.filter(user -> {
                if (user instanceof CompanyRepresentative) {
                    return ((CompanyRepresentative) user).isApproved() == approved;
                }
                return false;
            });
        }

        if (department != null) {
            stream = stream.filter(user -> {
                if (user instanceof CareerCenterStaff) {
                    return ((CareerCenterStaff) user).getDepartment() != null &&
                            ((CareerCenterStaff) user).getDepartment().equalsIgnoreCase(department);
                }
                return false;
            });
        }

        if (major != null) {
            stream = stream.filter(user -> {
                if (user instanceof Student) {
                    return ((Student) user).getMajor() != null &&
                            ((Student) user).getMajor().equalsIgnoreCase(major);
                }
                return false;
            });
        }

        if (yearOfStudy != null) {
            stream = stream.filter(user -> {
                if (user instanceof Student) {
                    return ((Student) user).getYearOfStudy() == yearOfStudy;
                }
                return false;
            });
        }

        if (companyName != null) {
            stream = stream.filter(user -> {
                if (user instanceof CompanyRepresentative) {
                    return ((CompanyRepresentative) user).getCompanyName() != null &&
                            ((CompanyRepresentative) user).getCompanyName().equalsIgnoreCase(companyName);
                }
                return false;
            });
        }

        return stream;
    }

    /**
     * Creates a new builder for constructing a filter.
     *
     * @return a new filter builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for UserFilter.
     */
    public static class Builder {
        private final UserFilter filter;

        private Builder() {
            this.filter = new UserFilter();
        }

        public Builder role(UserRole role) {
            filter.role = role;
            return this;
        }

        public Builder nameContains(String text) {
            filter.nameContains = text;
            return this;
        }

        public Builder approved(Boolean approved) {
            filter.approved = approved;
            return this;
        }

        public Builder department(String department) {
            filter.department = department;
            return this;
        }

        public Builder major(String major) {
            filter.major = major;
            return this;
        }

        public Builder yearOfStudy(Integer year) {
            filter.yearOfStudy = year;
            return this;
        }

        public Builder companyName(String companyName) {
            filter.companyName = companyName;
            return this;
        }

        public UserFilter build() {
            return filter;
        }
    }

    // Getters for all fields
    public UserRole getRole() { return role; }
    public String getNameContains() { return nameContains; }
    public Boolean getApproved() { return approved; }
    public String getDepartment() { return department; }
    public String getMajor() { return major; }
    public Integer getYearOfStudy() { return yearOfStudy; }
    public String getCompanyName() { return companyName; }
}
