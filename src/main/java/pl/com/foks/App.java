package pl.com.foks;

import pl.com.foks.command.CommandFactory;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.user.UserRepository;
import pl.com.foks.repository.vehicle.VehicleRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private User currentUser;

    public App(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    public VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void save() {
        userRepository.save();
        vehicleRepository.save();
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);
        final CommandFactory commandFactory = new CommandFactory(this);

        logger.info("""
                Register user (register <name> <password>)
                Login (login <name> <password>)
                """);
        while (scanner.hasNextLine()) {
            final String[] command = scanner.nextLine().split(" ");
            if (command.length > 0) {
                commandFactory.createCommand(command[0]).accept(Arrays.copyOfRange(command, 1, command.length));
            }
        }
    }
}
