package code.data;

import code.model.*;
import code.repository.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * DataPersistenceManager handles the persistence of runtime data back to CSV files.
 * This ensures that changes made during the program execution are saved for future sessions.
 * 
 * Note: This class currently provides export functionality. In a production system,
 * you might want to add automatic save on shutdown or periodic saves.
 */
public class DataPersistenceManager {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;

    public DataPersistenceManager(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Exports all student data to a CSV file.
     *
     * @param filePath the path where the CSV file should be saved
     * @throws IOException if the file cannot be written
     */
    public void exportStudents(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("StudentID,Name,Major,Year,Email");
            writer.newLine();
            
            // Write student records
            userRepository.findAll().stream()
                    .filter(user -> user instanceof Student)
                    .map(user -> (Student) user)
                    .forEach(student -> {
                        try {
                            String line = String.format("%s,%s,%s,%d,%s@e.ntu.edu.sg",
                                    student.getId(),
                                    student.getName(),
                                    student.getMajor(),
                                    student.getYearOfStudy(),
                                    student.getId().toLowerCase());
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println("Error writing student record: " + student.getId());
                        }
                    });
        }
    }

    /**
     * Exports all career center staff data to a CSV file.
     *
     * @param filePath the path where the CSV file should be saved
     * @throws IOException if the file cannot be written
     */
    public void exportStaff(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("StaffID,Name,Role,Department,Email");
            writer.newLine();
            
            // Write staff records
            userRepository.findAll().stream()
                    .filter(user -> user instanceof CareerCenterStaff)
                    .map(user -> (CareerCenterStaff) user)
                    .forEach(staff -> {
                        try {
                            String line = String.format("%s,%s,Career Center Staff,%s,%s@ntu.edu.sg",
                                    staff.getId(),
                                    staff.getName(),
                                    staff.getDepartment(),
                                    staff.getId());
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println("Error writing staff record: " + staff.getId());
                        }
                    });
        }
    }

    /**
     * Exports all company representative data to a CSV file.
     *
     * @param filePath the path where the CSV file should be saved
     * @throws IOException if the file cannot be written
     */
    public void exportCompanyRepresentatives(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            writer.newLine();
            
            // Write company representative records
            userRepository.findAll().stream()
                    .filter(user -> user instanceof CompanyRepresentative)
                    .map(user -> (CompanyRepresentative) user)
                    .forEach(rep -> {
                        try {
                            String status = rep.isApproved() ? "Approved" : "Pending";
                            String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                                    rep.getId(),
                                    rep.getName(),
                                    rep.getCompanyName(),
                                    rep.getDepartment(),
                                    rep.getPosition(),
                                    rep.getId(),
                                    status);
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println("Error writing company representative record: " + rep.getId());
                        }
                    });
        }
    }

    /**
     * Exports all internship opportunities to a CSV file.
     *
     * @param filePath the path where the CSV file should be saved
     * @throws IOException if the file cannot be written
     */
    public void exportInternshipOpportunities(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("OpportunityID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,MaxSlots,ConfirmedSlots,Visible,CreatedByID");
            writer.newLine();
            
            // Write internship opportunity records
            internshipRepository.findAll().forEach(opportunity -> {
                try {
                    String line = String.format("%s,%s,\"%s\",%s,%s,%s,%s,%s,%s,%d,%d,%b,%s",
                            opportunity.getId(),
                            opportunity.getTitle(),
                            opportunity.getDescription().replace("\"", "\"\""), // Escape quotes
                            opportunity.getLevel(),
                            opportunity.getPreferredMajor(),
                            opportunity.getOpeningDate() != null ? opportunity.getOpeningDate().format(DATE_FORMATTER) : "",
                            opportunity.getClosingDate() != null ? opportunity.getClosingDate().format(DATE_FORMATTER) : "",
                            opportunity.getStatus(),
                            opportunity.getCompanyName(),
                            opportunity.getMaxSlots(),
                            opportunity.getConfirmedSlots(),
                            opportunity.isVisible(),
                            opportunity.getCreatedBy() != null ? opportunity.getCreatedBy().getId() : "");
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Error writing internship opportunity record: " + opportunity.getId());
                }
            });
        }
    }

    /**
     * Exports all internship applications to a CSV file.
     *
     * @param filePath the path where the CSV file should be saved
     * @throws IOException if the file cannot be written
     */
    public void exportInternshipApplications(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("ApplicationID,StudentID,OpportunityID,Status,SubmittedAt,PlacementAccepted");
            writer.newLine();
            
            // Write application records
            applicationRepository.findAll().forEach(application -> {
                try {
                    String line = String.format("%s,%s,%s,%s,%s,%b",
                            application.getId(),
                            application.getStudent().getId(),
                            application.getOpportunity().getId(),
                            application.getStatus(),
                            application.getSubmittedAt().format(DATETIME_FORMATTER),
                            application.isPlacementAccepted());
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Error writing application record: " + application.getId());
                }
            });
        }
    }

    /**
     * Exports all data to the default export directory.
     *
     * @param exportDir the directory where all CSV files should be saved
     * @throws IOException if any file cannot be written
     */
    public void exportAll(String exportDir) throws IOException {
        if (!exportDir.endsWith("/")) {
            exportDir += "/";
        }
        
        System.out.println("Exporting all data to: " + exportDir);
        
        exportStudents(exportDir + "student_list_export.csv");
        exportStaff(exportDir + "staff_list_export.csv");
        exportCompanyRepresentatives(exportDir + "company_representative_list_export.csv");
        exportInternshipOpportunities(exportDir + "internship_opportunities_export.csv");
        exportInternshipApplications(exportDir + "internship_applications_export.csv");
        
        System.out.println("Export completed successfully.");
    }
}
