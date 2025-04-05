package com.umcsuser.carrent.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import com.umcsuser.carrent.db.JsonFileStorage;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJsonRepository implements UserRepository {
    private final JsonFileStorage<User> storage;
    private final List<User> users;

    public UserJsonRepository() {
        storage = new JsonFileStorage<>("users.json", new TypeToken<List<User>>() {}.getType());
        this.users = new ArrayList<>(storage.load());
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
