package code.service;

import code.model.InternshipOpportunity;
import code.model.Student;
import code.repository.InternshipRepository;
import code.enums.InternshipLevel;

import java.util.List;
import java.util.stream.Collectors;

public class InternshipService {
    private final InternshipRepository internshipRepository;

    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public List<InternshipOpportunity> listAvailableInternships() {
        return internshipRepository.findAll()
                .stream()
                .filter(InternshipOpportunity::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Lists available internships filtered by student's year of study and major.
     * Year 1-2 students: BASIC level only
     * Year 3-4 students: All levels (BASIC, INTERMEDIATE, ADVANCED)
     * Major filter: Student's major must match preferredMajor (case-insensitive), or preferredMajor is null/empty
     */
    public List<InternshipOpportunity> listAvailableInternshipsForStudent(Student student) {
        int year = student.getYearOfStudy();
        String studentMajor = student.getMajor();
        return internshipRepository.findAll()
                .stream()
                .filter(InternshipOpportunity::isAvailable)
                .filter(opp -> isEligibleForLevel(year, opp.getLevel()))
                .filter(opp -> isEligibleForMajor(studentMajor, opp.getPreferredMajor()))
                .collect(Collectors.toList());
    }

    private boolean isEligibleForLevel(int yearOfStudy, InternshipLevel level) {
        // Year 1-2 students can only see BASIC level opportunities
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        // Year 3-4 students can see all levels
        return true;
    }

    private boolean isEligibleForMajor(String studentMajor, String preferredMajor) {
        // If no preferred major is specified, the opportunity is open to all majors
        if (preferredMajor == null || preferredMajor.trim().isEmpty()) {
            return true;
        }
        // Case-insensitive major matching
        return studentMajor != null && studentMajor.trim().equalsIgnoreCase(preferredMajor.trim());
    }
}
