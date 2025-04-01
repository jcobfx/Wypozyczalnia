package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import pl.com.foks.infrastructure.exceptions.UserNotFound;

@AllArgsConstructor
public class AuthService {
    private final UserService userService;

    public boolean login(AuthData authData) {
        String login = authData.getLogin();
        String password = authData.getPassword();
        User user = userService.getUserByLogin(login).orElseThrow(() -> new UserNotFound("User not found"));
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }

    public void register(AuthData authData) {
        String login = authData.getLogin();
        String password = authData.getPassword();
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(null, login, password, User.Role.USER);
        userService.createUser(user);
    }

    public interface AuthData {
        String getLogin();
        String getPassword();

        static AuthData of(String login, String password) {
            return new AuthData() {
                @Override
                public String getLogin() {
                    return login;
                }

                @Override
                public String getPassword() {
                    return password;
                }
            };
        }
    }
}
