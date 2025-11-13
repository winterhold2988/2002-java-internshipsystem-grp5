package code.service;

import code.model.InternshipOpportunity;
import code.repository.InternshipRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InternshipService {
    private final InternshipRepository internshipRepository;

    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public List<InternshipOpportunity> listAvailableInternships() {
        return internshipRepository.findAll()
                .stream()
                .filter(InternshipOpportunity::isAvailable)
                .collect(Collectors.toList());
    }
}
