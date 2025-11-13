package code.data;

import code.model.CareerCenterStaff;
import code.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads career center staff data from CSV files and populates the UserRepository.
 * CSV format: StaffID,Name,Role,Department,Email
 */
public class StaffDataLoader {

    private static final String DEFAULT_PASSWORD = "password";
    private static final String CSV_PATH = "data/staff_list.csv";

    private final UserRepository userRepository;

    public StaffDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads staff data from the default CSV file in resources.
     *
     * @return the number of staff members loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData() throws IOException {
        return loadData(CSV_PATH);
    }

    /**
     * Loads staff data from a specified CSV file path in resources.
     *
     * @param csvPath the path to the CSV file relative to resources directory
     * @return the number of staff members loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData(String csvPath) throws IOException {
        List<String[]> records = CSVReader.readFromResources(csvPath);
        List<CareerCenterStaff> staffMembers = parseStaff(records);
        
        for (CareerCenterStaff staff : staffMembers) {
            userRepository.save(staff);
        }
        
        return staffMembers.size();
    }

    /**
     * Parses CSV records into CareerCenterStaff objects.
     *
     * @param records the CSV records to parse
     * @return a list of CareerCenterStaff objects
     */
    private List<CareerCenterStaff> parseStaff(List<String[]> records) {
        List<CareerCenterStaff> staffMembers = new ArrayList<>();
        
        for (String[] record : records) {
            if (record.length < 5) {
                System.err.println("Invalid staff record (expected 5 fields): " + String.join(",", record));
                continue;
            }
            
            String staffId = record[0];
            String name = record[1];
            // Role is in record[2] but not currently used in the model
            String department = record[3];
            // Email is in record[4] but not currently used in the model
            
            CareerCenterStaff staff = new CareerCenterStaff(staffId, name, DEFAULT_PASSWORD, department);
            staffMembers.add(staff);
        }
        
        return staffMembers;
    }
}
