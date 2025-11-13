package code.config;

import code.repository.*;
import code.data.*;
import code.dto.*;

import java.io.IOException;

/**
 * AppConfig is the central configuration class for the application.
 * It initializes all repositories, data loaders, and utilities needed by the application.
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
                withdrawalRequestRepository
        );
        this.dataBootstrap.initialize();

        // Initialize data persistence manager
        this.dataPersistenceManager = new DataPersistenceManager(
                userRepository,
                internshipRepository,
                applicationRepository
        );

        // Initialize ID generator
        this.idGenerator = new IDGenerator(
                applicationRepository,
                internshipRepository,
                registrationRequestRepository,
                withdrawalRequestRepository
        );
        
        // Initialize report generator
        this.reportGenerator = new ReportGenerator(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository
        );
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
}
