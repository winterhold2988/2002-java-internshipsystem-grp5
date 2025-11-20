package code.data;

import code.model.CompanyRepresentative;
import code.model.RegistrationRequest;
import code.repository.UserRepository;
import code.repository.RegistrationRequestRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads company representative data from CSV files and populates the UserRepository.
 * CSV format: CompanyRepID,Name,CompanyName,Department,Position,Email,Status
 */
public class CompanyRepresentativeDataLoader {

    private static final String DEFAULT_PASSWORD = "password";
    private static final String CSV_PATH = "data/company_representative_list.csv";

    private final UserRepository userRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final IDGenerator idGenerator;

    public CompanyRepresentativeDataLoader(UserRepository userRepository, 
                                          RegistrationRequestRepository registrationRequestRepository,
                                          IDGenerator idGenerator) {
        this.userRepository = userRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.idGenerator = idGenerator;
    }

    /**
     * Loads company representative data from the default CSV file in resources.
     *
     * @return the number of company representatives loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData() throws IOException {
        return loadData(CSV_PATH);
    }

    /**
     * Loads company representative data from a specified CSV file path in resources.
     *
     * @param csvPath the path to the CSV file relative to resources directory
     * @return the number of company representatives loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData(String csvPath) throws IOException {
        List<String[]> records = CSVReader.readFromResources(csvPath);
        List<CompanyRepresentative> representatives = parseRepresentatives(records);
        
        for (CompanyRepresentative rep : representatives) {
            userRepository.save(rep);
        }
        
        return representatives.size();
    }

    /**
     * Parses CSV records into CompanyRepresentative objects.
     *
     * @param records the CSV records to parse
     * @return a list of CompanyRepresentative objects
     */
    private List<CompanyRepresentative> parseRepresentatives(List<String[]> records) {
        List<CompanyRepresentative> representatives = new ArrayList<>();
        
        for (String[] record : records) {
            if (record.length < 7) {
                System.err.println("Invalid company representative record (expected 7 fields): " + String.join(",", record));
                continue;
            }
            
            String repId = record[0];
            String name = record[1];
            String companyName = record[2];
            String department = record[3];
            String position = record[4];
            // Email is in record[5] but not currently used in the model
            String status = record[6];
            
            CompanyRepresentative rep = new CompanyRepresentative(repId, name, DEFAULT_PASSWORD);
            rep.setCompanyName(companyName);
            rep.setDepartment(department);
            rep.setPosition(position);
            
            // Parse approval status
            boolean approved = status.equalsIgnoreCase("Approved");
            rep.setApproved(approved);
            
            representatives.add(rep);
            
            // Create RegistrationRequest for pending representatives
            if (!approved && registrationRequestRepository != null && idGenerator != null) {
                String requestId = idGenerator.generateRequestId();
                RegistrationRequest request = new RegistrationRequest(requestId, rep);
                registrationRequestRepository.save(request);
            }
        }
        
        return representatives;
    }
}
