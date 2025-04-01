package pl.com.foks.infrastructure.repository;

import com.google.gson.reflect.TypeToken;
import pl.com.foks.domain.User;
import pl.com.foks.infrastructure.utils.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    private final JsonFileStorage<User> storage;
    private final List<User> users;

    public UserRepository(String filename) {
        storage = new JsonFileStorage<>(filename, new TypeToken<ArrayList<User>>() {}.getType());
        this.users = storage.load();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }
        users.add(user);
        storage.save(users);
        return user;
    }

    @Override
    public void deleteById(String id) {
        users.removeIf(user -> user.getId().equals(id));
        storage.save(users);
    }
}
