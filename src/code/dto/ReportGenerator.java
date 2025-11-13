package code.dto;

import code.enums.ApplicationStatus;
import code.enums.OpportunityStatus;
import code.model.*;
import code.repository.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory class for generating various reports from repository data.
 * Bridges the gap between repositories and DTOs.
 */
public class ReportGenerator {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;

    public ReportGenerator(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.withdrawalRequestRepository = withdrawalRequestRepository;
    }

    /**
     * Generates a comprehensive system statistics report.
     *
     * @return a SystemStatisticsReport
     */
    public SystemStatisticsReport generateSystemStatistics() {
        // Count users by type
        long students = userRepository.findAll().stream()
                .filter(u -> u instanceof Student)
                .count();
        
        long staff = userRepository.findAll().stream()
                .filter(u -> u instanceof CareerCenterStaff)
                .count();
        
        long totalReps = userRepository.findAll().stream()
                .filter(u -> u instanceof CompanyRepresentative)
                .count();
        
        long approvedReps = userRepository.findAll().stream()
                .filter(u -> u instanceof CompanyRepresentative)
                .map(u -> (CompanyRepresentative) u)
                .filter(CompanyRepresentative::isApproved)
                .count();

        // Count opportunities by status
        Map<OpportunityStatus, Long> oppStats = internshipRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        InternshipOpportunity::getStatus,
                        Collectors.counting()
                ));

        // Count applications by status
        Map<ApplicationStatus, Long> appStats = applicationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        InternshipApplication::getStatus,
                        Collectors.counting()
                ));

        // Calculate slot statistics
        long totalSlots = internshipRepository.findAll().stream()
                .mapToLong(InternshipOpportunity::getMaxSlots)
                .sum();
        
        long filledSlots = internshipRepository.findAll().stream()
                .mapToLong(InternshipOpportunity::getConfirmedSlots)
                .sum();

        return SystemStatisticsReport.builder()
                .totalStudents(students)
                .totalStaff(staff)
                .totalCompanyReps(totalReps)
                .approvedCompanyReps(approvedReps)
                .totalOpportunities(internshipRepository.findAll().size())
                .opportunitiesByStatus(oppStats)
                .totalApplications(applicationRepository.findAll().size())
                .applicationsByStatus(appStats)
                .pendingRegistrationRequests(registrationRequestRepository.findAll().size())
                .pendingWithdrawalRequests(withdrawalRequestRepository.findAll().size())
                .totalAvailableSlots(totalSlots)
                .totalFilledSlots(filledSlots)
                .build();
    }

    /**
     * Generates a company activity report for a specific company representative.
     *
     * @param representative the company representative
     * @return a CompanyActivityReport
     */
    public CompanyActivityReport generateCompanyReport(CompanyRepresentative representative) {
        // Find all opportunities created by this rep
        List<OpportunityReportDTO> opportunities = internshipRepository.findAll().stream()
                .filter(opp -> opp.getCreatedBy() != null && 
                        opp.getCreatedBy().getId().equals(representative.getId()))
                .map(OpportunityReportDTO::from)
                .collect(Collectors.toList());

        // Find all applications for these opportunities
        List<String> opportunityIds = opportunities.stream()
                .map(OpportunityReportDTO::getId)
                .collect(Collectors.toList());

        List<ApplicationReportDTO> applications = applicationRepository.findAll().stream()
                .filter(app -> opportunityIds.contains(app.getOpportunity().getId()))
                .map(ApplicationReportDTO::from)
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .collect(Collectors.toList());

        // Calculate statistics
        int totalSlots = opportunities.stream()
                .mapToInt(OpportunityReportDTO::getMaxSlots)
                .sum();
        
        int filledSlots = opportunities.stream()
                .mapToInt(OpportunityReportDTO::getConfirmedSlots)
                .sum();

        long pending = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .count();
        
        long successful = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .count();

        return CompanyActivityReport.builder()
                .companyName(representative.getCompanyName())
                .representativeName(representative.getName())
                .opportunities(opportunities)
                .applications(applications)
                .totalSlots(totalSlots)
                .filledSlots(filledSlots)
                .pendingApplications((int) pending)
                .successfulApplications((int) successful)
                .build();
    }

    /**
     * Generates a student activity report for a specific student.
     *
     * @param student the student
     * @return a StudentActivityReport
     */
    public StudentActivityReport generateStudentReport(Student student) {
        // Find all applications by this student
        List<ApplicationReportDTO> applications = applicationRepository.findAll().stream()
                .filter(app -> app.getStudent().getId().equals(student.getId()))
                .map(ApplicationReportDTO::from)
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .collect(Collectors.toList());

        // Calculate statistics
        long pending = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .count();
        
        long successful = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .count();
        
        long unsuccessful = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.UNSUCCESSFUL)
                .count();

        boolean hasAccepted = applications.stream()
                .anyMatch(ApplicationReportDTO::isPlacementAccepted);

        return StudentActivityReport.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .major(student.getMajor())
                .yearOfStudy(student.getYearOfStudy())
                .applications(applications)
                .totalApplications(applications.size())
                .pendingApplications((int) pending)
                .successfulApplications((int) successful)
                .unsuccessfulApplications((int) unsuccessful)
                .hasAcceptedPlacement(hasAccepted)
                .build();
    }

    /**
     * Generates a list of opportunity report DTOs based on a filter.
     *
     * @param filter the filter to apply
     * @return a list of opportunity report DTOs
     */
    public List<OpportunityReportDTO> generateOpportunityList(InternshipOpportunityFilter filter) {
        return filter.apply(internshipRepository.findAll())
                .map(OpportunityReportDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Generates a list of application report DTOs based on a filter.
     *
     * @param filter the filter to apply
     * @return a list of application report DTOs
     */
    public List<ApplicationReportDTO> generateApplicationList(InternshipApplicationFilter filter) {
        return filter.apply(applicationRepository.findAll())
                .map(ApplicationReportDTO::from)
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Generates a list of user report DTOs based on a filter.
     *
     * @param filter the filter to apply
     * @return a list of user report DTOs
     */
    public List<UserReportDTO> generateUserList(UserFilter filter) {
        return filter.apply(userRepository.findAll())
                .map(UserReportDTO::from)
                .collect(Collectors.toList());
    }
}
