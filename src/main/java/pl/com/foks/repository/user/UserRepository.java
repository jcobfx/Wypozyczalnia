package pl.com.foks.repository.user;

import org.apache.commons.codec.digest.DigestUtils;
import pl.com.foks.data.IRepositoryDataManager;
import pl.com.foks.exceptions.FailedRepositoryLoadException;
import pl.com.foks.exceptions.FailedRepositorySaveException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    private final Authentication authentication;
    private final List<User> users;
    private final IRepositoryDataManager<User> dataManager;

    private int idCounter = 0;

    public UserRepository(IRepositoryDataManager<User> dataManager) {
        authentication = new Authentication();
        users = new ArrayList<>();
        this.dataManager = dataManager;

        register(User.Role.ADMIN, "admin", "admin");
    }

    public Optional<User> register(User.Role role, String login, String password) {
        if (users.stream().anyMatch(u -> Objects.equals(u.getLogin(), login))) {
            return Optional.empty();
        }
        final User user = new User(idCounter++, role, login, DigestUtils.sha256Hex(password));
        users.add(user);
        return Optional.of(user);
    }

    private boolean register(User user) {
        if (users.stream().anyMatch(u -> u.getIdentifier() == user.getIdentifier() || Objects.equals(u.getLogin(), user.getLogin()))) {
            return false;
        }
        users.add(user);
        return true;
    }

    @Override
    public Optional<User> getUser(int id) {
        return users.stream().filter(user -> user.getIdentifier() == id).findFirst();
    }

    @Override
    public List<User> getUsers() {
        final List<User> usersCopy = new ArrayList<>();
        for (User user : users) {
            usersCopy.add(user.deepClone());
        }
        return usersCopy;
    }

    @Override
    public void save() {
        try {
            dataManager.save(users);
        } catch (Exception e) {
            throw new FailedRepositorySaveException("Failed to save users", e);
        }
    }

    @Override
    public void load() {
        try {
            dataManager.load().forEach(this::register);
            idCounter = users.stream().map(User::getIdentifier).max(Integer::compareTo).orElse(0) + 1;
        } catch (Exception e) {
            throw new FailedRepositoryLoadException("Failed to load users", e);
        }
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public class Authentication {
        public Optional<User> authenticate(String username, String password) {
            String hashedPassword = DigestUtils.sha256Hex(password);
            return users.stream().filter(user -> user.getLogin().equals(username) && user.getPassword().equals(hashedPassword))
                    .findFirst();
        }
    }
}
