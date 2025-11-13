package code.repository;

import code.model.WithdrawalRequest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Repository for managing withdrawal requests from students.
public class WithdrawalRequestRepository {

    private final Map<String, WithdrawalRequest> requests = new LinkedHashMap<>();

    public void save(WithdrawalRequest request) {
        requests.put(request.getId(), request);
    }

    public Optional<WithdrawalRequest> findById(String id) {
        return Optional.ofNullable(requests.get(id));
    }

    public Collection<WithdrawalRequest> findAll() {
        return requests.values();
    }
}
