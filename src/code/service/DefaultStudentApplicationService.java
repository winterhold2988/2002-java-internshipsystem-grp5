package code.service;

import code.enums.ApplicationStatus;
import code.model.InternshipApplication;
import code.model.InternshipOpportunity;
import code.model.Student;
import code.model.WithdrawalRequest;
import code.repository.ApplicationRepository;
import code.repository.InternshipRepository;
import code.repository.WithdrawalRequestRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Concrete implementation coordinating repositories for student actions (SOLID - SRP).
 */
public class DefaultStudentApplicationService implements StudentApplicationService {

    private final ApplicationRepository applicationRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final InternshipRepository internshipRepository;

    public DefaultStudentApplicationService(
            ApplicationRepository applicationRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipRepository internshipRepository) {
        this.applicationRepository = applicationRepository;
        this.withdrawalRequestRepository = withdrawalRequestRepository;
        this.internshipRepository = internshipRepository;
    }

    @Override
    public List<InternshipApplication> getApplications(Student student) {
        return applicationRepository.findByStudent(student)
                .collect(Collectors.toList());
    }

    @Override
    public List<InternshipApplication> getPendingApplications(Student student) {
        return applicationRepository.findByStudent(student)
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<InternshipApplication> getApplicationsAwaitingDecision(Student student) {
        return applicationRepository.findByStudent(student)
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .filter(app -> !app.isPlacementAccepted())
                .collect(Collectors.toList());
    }

    @Override
    public List<InternshipApplication> getWithdrawableApplications(Student student) {
        return applicationRepository.findByStudent(student)
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING || 
                              app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InternshipOpportunity> findOpportunityById(String opportunityId) {
        return internshipRepository.findById(opportunityId);
    }

    @Override
    public boolean hasExistingApplication(Student student, InternshipOpportunity opportunity) {
        return applicationRepository.findByStudent(student)
                .anyMatch(app -> app.getOpportunity().getId().equals(opportunity.getId()));
    }

    @Override
    public InternshipApplication submitApplication(Student student, InternshipOpportunity opportunity) {
        String appId = "APP" + System.currentTimeMillis();
        InternshipApplication application = new InternshipApplication(appId, student, opportunity);
        applicationRepository.save(application);
        return application;
    }

    @Override
    public WithdrawalRequest requestWithdrawal(InternshipApplication application, String reason) {
        String requestId = "WR" + System.currentTimeMillis();
        WithdrawalRequest request = new WithdrawalRequest(requestId, application, reason);
        withdrawalRequestRepository.save(request);
        application.setStatus(ApplicationStatus.WITHDRAWAL_REQUESTED);
        applicationRepository.save(application);
        return request;
    }

    @Override
    public void acceptPlacement(InternshipApplication application) {
        application.setPlacementAccepted(true);
        applicationRepository.save(application);
    }
}
