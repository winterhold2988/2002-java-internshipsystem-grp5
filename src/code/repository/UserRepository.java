package code.repository;

import code.model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// Repository for managing users in the internship system.
public class UserRepository {

    private final Map<String, User> users = new LinkedHashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
