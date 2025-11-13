package code.repository;

import code.model.InternshipApplication;
import code.model.Student;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

// Repo is needed so that CLI wont rescan .csv, but use in-memory storage.
// Repository for managing internship applications.
public class ApplicationRepository {

    //"A001" → InternshipApplication{id="A001", student=Student(U2345123F), ...}
    private final Map<String, InternshipApplication> applications = new LinkedHashMap<>();

    // Save or update an internship application.
    public void save(InternshipApplication application) {
        applications.put(application.getId(), application);
    }

    // Find an internship application by its ID.
    public Optional<InternshipApplication> findById(String id) {
        return Optional.ofNullable(applications.get(id));
    }

    // Retrieve all internship applications.
    public Collection<InternshipApplication> findAll() {
        return applications.values();
    }
    
    // Find all applications submitted by a specific student.
    public Stream<InternshipApplication> findByStudent(Student student) {
        return applications.values().stream().filter(app -> app.getStudent().equals(student));
    }
}
