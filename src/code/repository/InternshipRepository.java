package edu.ntu.ccds.sc2002.internship.repository;

import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Repository for managing internship opportunities.
public class InternshipRepository {

    private final Map<String, InternshipOpportunity> opportunities = new LinkedHashMap<>();

    public void save(InternshipOpportunity opportunity) {
        opportunities.put(opportunity.getId(), opportunity);
    }

    public Optional<InternshipOpportunity> findById(String id) {
        return Optional.ofNullable(opportunities.get(id));
    }

    public Collection<InternshipOpportunity> findAll() {
        return opportunities.values();
    }
}
