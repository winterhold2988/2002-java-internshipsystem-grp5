package code.data;

import code.repository.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * IDGenerator provides methods to generate unique IDs for various entities in the system.
 * It ensures that IDs are unique and follow the expected format for each entity type.
 */
public class IDGenerator {

    private final AtomicInteger applicationCounter;
    private final AtomicInteger opportunityCounter;
    private final AtomicInteger requestCounter;
    private final AtomicInteger withdrawalCounter;

    /**
     * Creates a new IDGenerator and initializes counters based on existing data in repositories.
     *
     * @param applicationRepository the application repository
     * @param internshipRepository the internship repository
     * @param registrationRequestRepository the registration request repository
     * @param withdrawalRequestRepository the withdrawal request repository
     */
    public IDGenerator(
            ApplicationRepository applicationRepository,
            InternshipRepository internshipRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository) {
        
        // Initialize counters based on existing data
        this.applicationCounter = new AtomicInteger(calculateNextCounter(applicationRepository, "APP"));
        this.opportunityCounter = new AtomicInteger(calculateNextCounter(internshipRepository, "OPP"));
        this.requestCounter = new AtomicInteger(calculateNextCounter(registrationRequestRepository, "REQ"));
        this.withdrawalCounter = new AtomicInteger(calculateNextCounter(withdrawalRequestRepository, "WDR"));
    }

    /**
     * Generates a unique ID for an internship application.
     * Format: APP0001, APP0002, etc.
     *
     * @return a unique application ID
     */
    public String generateApplicationId() {
        return String.format("APP%04d", applicationCounter.getAndIncrement());
    }

    /**
     * Generates a unique ID for an internship opportunity.
     * Format: OPP0001, OPP0002, etc.
     *
     * @return a unique opportunity ID
     */
    public String generateOpportunityId() {
        return String.format("OPP%04d", opportunityCounter.getAndIncrement());
    }

    /**
     * Generates a unique ID for a registration request.
     * Format: REQ0001, REQ0002, etc.
     *
     * @return a unique request ID
     */
    public String generateRequestId() {
        return String.format("REQ%04d", requestCounter.getAndIncrement());
    }

    /**
     * Generates a unique ID for a withdrawal request.
     * Format: WDR0001, WDR0002, etc.
     *
     * @return a unique withdrawal ID
     */
    public String generateWithdrawalId() {
        return String.format("WDR%04d", withdrawalCounter.getAndIncrement());
    }

    /**
     * Calculates the next counter value based on existing IDs in a repository.
     * This method is used during initialization to ensure generated IDs don't conflict
     * with existing ones.
     *
     * @param repository the repository containing existing entities
     * @param prefix the prefix used for IDs in this repository
     * @return the next counter value to use
     */
    private int calculateNextCounter(Object repository, String prefix) {
        int maxId = 0;
        
        try {
            java.util.Collection<?> entities = null;
            
            if (repository instanceof ApplicationRepository) {
                entities = ((ApplicationRepository) repository).findAll();
            } else if (repository instanceof InternshipRepository) {
                entities = ((InternshipRepository) repository).findAll();
            } else if (repository instanceof RegistrationRequestRepository) {
                entities = ((RegistrationRequestRepository) repository).findAll();
            } else if (repository instanceof WithdrawalRequestRepository) {
                entities = ((WithdrawalRequestRepository) repository).findAll();
            }
            
            if (entities != null) {
                for (Object entity : entities) {
                    String id = null;
                    
                    if (entity instanceof code.model.InternshipApplication) {
                        id = ((code.model.InternshipApplication) entity).getId();
                    } else if (entity instanceof code.model.InternshipOpportunity) {
                        id = ((code.model.InternshipOpportunity) entity).getId();
                    } else if (entity instanceof code.model.RegistrationRequest) {
                        id = ((code.model.RegistrationRequest) entity).getId();
                    } else if (entity instanceof code.model.WithdrawalRequest) {
                        id = ((code.model.WithdrawalRequest) entity).getId();
                    }
                    
                    if (id != null && id.startsWith(prefix)) {
                        try {
                            int numericPart = Integer.parseInt(id.substring(prefix.length()));
                            maxId = Math.max(maxId, numericPart);
                        } catch (NumberFormatException e) {
                            // Skip IDs that don't follow the expected format
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating next counter for prefix " + prefix + ": " + e.getMessage());
        }
        
        return maxId + 1;
    }

    /**
     * Resets all counters. This should only be used for testing purposes.
     */
    public void resetCounters() {
        applicationCounter.set(1);
        opportunityCounter.set(1);
        requestCounter.set(1);
        withdrawalCounter.set(1);
    }
}
