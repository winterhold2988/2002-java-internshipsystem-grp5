package code.service;

import code.model.InternshipApplication;
import code.model.InternshipOpportunity;
import code.model.Student;
import code.model.WithdrawalRequest;

import java.util.List;
import java.util.Optional;

/**
 * Defines the application-related operations a student can perform.
 * Acts as an abstraction so the CLI depends on behaviour rather than repositories (SOLID - DIP).
 */
public interface StudentApplicationService {

    List<InternshipApplication> getApplications(Student student);

    List<InternshipApplication> getPendingApplications(Student student);

    List<InternshipApplication> getApplicationsAwaitingDecision(Student student);

    List<InternshipApplication> getWithdrawableApplications(Student student);

    Optional<InternshipOpportunity> findOpportunityById(String opportunityId);

    boolean hasExistingApplication(Student student, InternshipOpportunity opportunity);

    InternshipApplication submitApplication(Student student, InternshipOpportunity opportunity);

    WithdrawalRequest requestWithdrawal(InternshipApplication application, String reason);

    void acceptPlacement(InternshipApplication application);
}
