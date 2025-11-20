package code.data;

import code.repository.*;

import java.io.IOException;

/**
 * DataBootstrap coordinates the loading of all initial data into the application.
 * It ensures that all CSV data is loaded into the appropriate repositories when
 * the application starts.
 */
public class DataBootstrap {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;

    public DataBootstrap(
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
     * Initializes all data from CSV files into the repositories.
     * This should be called once at application startup.
     *
     * @throws IOException if any CSV file cannot be read
     */
    public void initialize() throws IOException {
        System.out.println("Initializing application data...");
        
        // Load user data
        int studentCount = loadStudents();
        int staffCount = loadStaff();
        int repCount = loadCompanyRepresentatives();
        
        System.out.println("Data initialization complete:");
        System.out.println("  - " + studentCount + " students loaded");
        System.out.println("  - " + staffCount + " staff members loaded");
        System.out.println("  - " + repCount + " company representatives loaded");
    }

    /**
     * Loads student data from CSV.
     *
     * @return the number of students loaded
     * @throws IOException if the CSV file cannot be read
     */
    private int loadStudents() throws IOException {
        StudentDataLoader loader = new StudentDataLoader(userRepository);
        return loader.loadData();
    }

    /**
     * Loads career center staff data from CSV.
     *
     * @return the number of staff members loaded
     * @throws IOException if the CSV file cannot be read
     */
    private int loadStaff() throws IOException {
        StaffDataLoader loader = new StaffDataLoader(userRepository);
        return loader.loadData();
    }

    /**
     * Loads company representative data from CSV.
     *
     * @return the number of company representatives loaded
     * @throws IOException if the CSV file cannot be read
     */
    private int loadCompanyRepresentatives() throws IOException {
        IDGenerator idGenerator = new IDGenerator(applicationRepository, internshipRepository, 
                                                   registrationRequestRepository, withdrawalRequestRepository);
        CompanyRepresentativeDataLoader loader = new CompanyRepresentativeDataLoader(
            userRepository, registrationRequestRepository, idGenerator);
        return loader.loadData();
    }

    /**
     * Gets the user repository.
     *
     * @return the user repository
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Gets the internship repository.
     *
     * @return the internship repository
     */
    public InternshipRepository getInternshipRepository() {
        return internshipRepository;
    }

    /**
     * Gets the application repository.
     *
     * @return the application repository
     */
    public ApplicationRepository getApplicationRepository() {
        return applicationRepository;
    }

    /**
     * Gets the registration request repository.
     *
     * @return the registration request repository
     */
    public RegistrationRequestRepository getRegistrationRequestRepository() {
        return registrationRequestRepository;
    }

    /**
     * Gets the withdrawal request repository.
     *
     * @return the withdrawal request repository
     */
    public WithdrawalRequestRepository getWithdrawalRequestRepository() {
        return withdrawalRequestRepository;
    }
}
