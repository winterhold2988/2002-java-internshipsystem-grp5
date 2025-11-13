package code.model;

import code.enums.UserRole;

// Represents a student user in the internship system.
public class Student extends User {

    private int yearOfStudy;
    private String major;

    public Student(String id, String name, String password, int yearOfStudy, String major) {
        super(id, name, password, UserRole.STUDENT);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
