package pl.com.foks.data;

import pl.com.foks.exceptions.RentalException;
import pl.com.foks.repository.user.User;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserDataManager implements IRepositoryDataManager<User> {
    private final Path users;

    /**
     * Creates a new instance of the UserDataManager
     * @param users path to the file with user data
     */
    public UserDataManager(Path users) {
        if (!users.toFile().exists()) {
            throw new RentalException("User data file does not exist");
        }
        this.users = users;
    }

    @Override
    public void save(List<User> userList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(users.toFile(), false));
        List<String> usersCSV = new ArrayList<>();
        userList.forEach(user -> usersCSV.add(user.toCSV()));
        try {
            writer.write(String.join("\n", usersCSV));
            writer.flush();
        } catch (IOException e) {
            throw new RentalException("Cannot write to file", e);
        }
    }

    @Override
    public List<User> load() throws FileNotFoundException {
        final List<User> userList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(users.toFile()));
        reader.lines().forEach(line -> {
            final String[] split = line.split(";");
            userList.add(User.fromCSV(split));
        });
        return userList;
    }
}
