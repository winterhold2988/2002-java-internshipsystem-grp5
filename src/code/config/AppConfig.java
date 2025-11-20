package code.config;

import code.data.*;
import code.dto.*;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterService;
import code.repository.*;
import code.service.DefaultStudentApplicationService;
import code.service.InternshipService;
import code.service.StudentApplicationService;

import java.io.IOException;

/**
 * AppConfig is the central configuration class for the application.
 * It initializes all repositories, data loaders, and utilities needed by the
 * application.
 */
public class AppConfig {

    // Repositories
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;

    // Data management
    private final DataBootstrap dataBootstrap;
    private final DataPersistenceManager dataPersistenceManager;
    private final IDGenerator idGenerator;

    // Report generation
    private final ReportGenerator reportGenerator;
    // Services
    private final InternshipService internshipService;
    private final StudentApplicationService studentApplicationService;
    // Filter components
    private final OpportunityFilterService opportunityFilterService;
    private final FilterStateManager filterStateManager;

    /**
     * Initializes the application configuration and loads initial data.
     *
     * @throws IOException if CSV files cannot be read during initialization
     */
    public AppConfig() throws IOException {
        // Initialize repositories
        this.userRepository = new UserRepository();
        this.internshipRepository = new InternshipRepository();
        this.applicationRepository = new ApplicationRepository();
        this.registrationRequestRepository = new RegistrationRequestRepository();
        this.withdrawalRequestRepository = new WithdrawalRequestRepository();

        // Initialize data bootstrap and load initial data
        this.dataBootstrap = new DataBootstrap(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository);
        this.dataBootstrap.initialize();

        // Initialize data persistence manager
        this.dataPersistenceManager = new DataPersistenceManager(
                userRepository,
                internshipRepository,
                applicationRepository);

        // Initialize ID generator
        this.idGenerator = new IDGenerator(
                applicationRepository,
                internshipRepository,
                registrationRequestRepository,
                withdrawalRequestRepository);
        // Initialize services
        this.internshipService = new InternshipService(this.internshipRepository);
        this.studentApplicationService = new DefaultStudentApplicationService(
                this.applicationRepository,
                this.withdrawalRequestRepository,
                this.internshipRepository);

        // Initialize report generator
        this.reportGenerator = new ReportGenerator(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository);
        
        // Initialize filter components
        this.opportunityFilterService = new OpportunityFilterService();
        this.filterStateManager = new FilterStateManager();
    }

    // Getters for repositories
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public InternshipRepository getInternshipRepository() {
        return internshipRepository;
    }

    public ApplicationRepository getApplicationRepository() {
        return applicationRepository;
    }

    public RegistrationRequestRepository getRegistrationRequestRepository() {
        return registrationRequestRepository;
    }

    public WithdrawalRequestRepository getWithdrawalRequestRepository() {
        return withdrawalRequestRepository;
    }

    // Getters for data management utilities
    public DataBootstrap getDataBootstrap() {
        return dataBootstrap;
    }

    public DataPersistenceManager getDataPersistenceManager() {
        return dataPersistenceManager;
    }

    public IDGenerator getIdGenerator() {
        return idGenerator;
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public InternshipService getInternshipService() {
        return internshipService;
    }

    public StudentApplicationService getStudentApplicationService() {
        return studentApplicationService;
    }

    public OpportunityFilterService getOpportunityFilterService() {
        return opportunityFilterService;
    }

    public FilterStateManager getFilterStateManager() {
        return filterStateManager;
    }
}
