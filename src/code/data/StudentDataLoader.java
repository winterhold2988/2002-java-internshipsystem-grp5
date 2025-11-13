package code.data;

import code.model.Student;
import code.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads student data from CSV files and populates the UserRepository.
 * CSV format: StudentID,Name,Major,Year,Email
 */
public class StudentDataLoader {

    private static final String DEFAULT_PASSWORD = "password";
    private static final String CSV_PATH = "data/student_list.csv";

    private final UserRepository userRepository;

    public StudentDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads student data from the default CSV file in resources.
     *
     * @return the number of students loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData() throws IOException {
        return loadData(CSV_PATH);
    }

    /**
     * Loads student data from a specified CSV file path in resources.
     *
     * @param csvPath the path to the CSV file relative to resources directory
     * @return the number of students loaded
     * @throws IOException if the CSV file cannot be read
     */
    public int loadData(String csvPath) throws IOException {
        List<String[]> records = CSVReader.readFromResources(csvPath);
        List<Student> students = parseStudents(records);
        
        for (Student student : students) {
            userRepository.save(student);
        }
        
        return students.size();
    }

    /**
     * Parses CSV records into Student objects.
     *
     * @param records the CSV records to parse
     * @return a list of Student objects
     */
    private List<Student> parseStudents(List<String[]> records) {
        List<Student> students = new ArrayList<>();
        
        for (String[] record : records) {
            if (record.length < 5) {
                System.err.println("Invalid student record (expected 5 fields): " + String.join(",", record));
                continue;
            }
            
            try {
                String studentId = record[0];
                String name = record[1];
                String major = record[2];
                int year = Integer.parseInt(record[3]);
                // Email is in record[4] but not currently used in the Student model
                
                Student student = new Student(studentId, name, DEFAULT_PASSWORD, year, major);
                students.add(student);
                
            } catch (NumberFormatException e) {
                System.err.println("Invalid year format in student record: " + String.join(",", record));
            }
        }
        
        return students;
    }
}
