package code.repository;

import code.model.RegistrationRequest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Repository for managing registration requests from company representatives.
public class RegistrationRequestRepository {

    private final Map<String, RegistrationRequest> requests = new LinkedHashMap<>();

    public void save(RegistrationRequest request) {
        requests.put(request.getId(), request);
    }

    public Optional<RegistrationRequest> findById(String id) {
        return Optional.ofNullable(requests.get(id));
    }

    public Collection<RegistrationRequest> findAll() {
        return requests.values();
    }
}
