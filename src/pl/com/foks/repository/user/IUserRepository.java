package pl.com.foks.repository.user;

import pl.com.foks.repository.IRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends IRepository {
    Optional<User> getUser(int id);
    List<User> getUsers();
}
