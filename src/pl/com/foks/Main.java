package pl.com.foks;

import pl.com.foks.data.UserDataManager;
import pl.com.foks.data.VehicleDataManager;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.vehicle.VehicleFactory;
import pl.com.foks.repository.vehicle.VehicleRepository;
import pl.com.foks.repository.user.UserRepository;
import pl.com.foks.repository.vehicle.vehicles.Car;
import pl.com.foks.repository.vehicle.vehicles.Motorcycle;
import pl.com.foks.util.DataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String VEHICLES_FILE = "vehicles.csv";
    private static final String USERS_FILE = "users.csv";

    public static final Path WORKSPACE = Path.of(System.getProperty("user.home"), "wypozyczalnia");
    public static final Path VEHICLES = WORKSPACE.resolve(VEHICLES_FILE);
    public static final Path USERS = WORKSPACE.resolve(USERS_FILE);

    private static VehicleRepository vehicleRepository = null;
    private static UserRepository userRepository = null;
    private static User currentUser = null;

    public static void main(String[] args) throws IOException {
        VehicleFactory.registerVehicleClass(Car.class);
        VehicleFactory.registerVehicleClass(Motorcycle.class);

        init();
        run();
    }

    /**
     * Initializes the program
     * @throws IOException if the file cannot be read
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void init() throws IOException {
        if (!WORKSPACE.toFile().exists()) {
            WORKSPACE.toFile().mkdirs();
        }

        InputStream vehicles = Main.class.getClassLoader().getResourceAsStream(VEHICLES_FILE);
        if (vehicles != null) {
            if (VEHICLES.toFile().exists()) VEHICLES.toFile().delete();
            Files.copy(vehicles, VEHICLES);
        }

        InputStream users = Main.class.getClassLoader().getResourceAsStream(USERS_FILE);
        if (users != null) {
            if (USERS.toFile().exists()) USERS.toFile().delete();
            Files.copy(users, USERS);
        }

        vehicleRepository = new VehicleRepository(new VehicleDataManager(VEHICLES));
        vehicleRepository.load();

        userRepository = new UserRepository(new UserDataManager(USERS));
        userRepository.load();
    }

    /**
     * Runs the program
     */
    private static void run() {
        final Scanner scanner = new Scanner(System.in);
        logger.info("""
                Register user (register <name> <password>)
                Login (login <name> <password>)
                """);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final String[] split = line.split(" ");
            if (currentUser == null) {
                switch (split[0]) {
                    case "exit": {
                        exit();
                        break;
                    }
                    case "login": {
                        DataUtils.isDataValidThen(split, 3, data -> login(data[1], data[2]));
                        break;
                    }
                    case "register": {
                        DataUtils.isDataValidThen(split, 3, data -> register(data[1], data[2]));
                        break;
                    }
                    default: {
                        help();
                    }
                }
            } else {
                switch (split[0]) {
                    case "exit": {
                        exit();
                        break;
                    }
                    case "logout": {
                        logout();
                        break;
                    }
                    case "user": {
                        DataUtils.isDataValidThen(split, 1, data -> logger.info(currentUser.toString()));
                        if (currentUser.getRole() == User.Role.ADMIN) {
                            DataUtils.isDataValidThen(split, 2, data -> user(Integer.parseInt(data[1])));
                        }
                        break;
                    }
                    case "vehicles": {
                        vehicles();
                        break;
                    }
                    case "rent": {
                        DataUtils.isDataValidThen(split, 2, data -> rent(Integer.parseInt(data[1])));
                        break;
                    }
                    case "return": {
                        DataUtils.isDataValidThen(split, 2, data -> _return(Integer.parseInt(data[1])));
                        break;
                    }
                    case "add": {
                        if (currentUser.getRole() == User.Role.ADMIN) add();
                    }
                    case "remove": {
                        if (currentUser.getRole() == User.Role.ADMIN) remove();
                    }
                    case "users": {
                        if (currentUser.getRole() == User.Role.ADMIN) users();
                    }
                    default: {
                        help();
                    }
                }
            }
        }
    }

    /**
     * Registers a user
     * @param login login of the user
     * @param password password of the user
     */
    private static void register(String login, String password) {
        userRepository.register(User.Role.CLIENT, login, password).ifPresentOrElse(
                user -> {
                    currentUser = user;
                    logger.info("User registered");
                },
                () -> logger.warning("Cannot register user")
        );
    }

    /**
     * Logs in a user
     * @param login login of the user
     * @param password password of the user
     */
    private static void login(String login, String password) {
        userRepository.getAuthentication().authenticate(login, password).ifPresentOrElse(
                user -> {
                    currentUser = user;
                    logger.info("User logged in");
                },
                () -> logger.warning("Cannot login")
        );
    }

    /**
     * Logs out the user
     */
    private static void logout() {
        currentUser = null;
        logger.info("User logged out");
        logger.info("""
                    Register user (register <name> <password>)
                    Login (login <name> <password>)
                    """);
    }

    /**
     * Exits the program
     */
    private static void exit() {
        vehicleRepository.save();
        userRepository.save();
        System.exit(0);
    }

    /**
     * Displays help
     */
    private static void help() {
        if (currentUser != null) {
            logger.info("Available commands: help, exit, logout, user, vehicles, rent <vehicleId>, return <vehicleId>");
            if (currentUser.getRole() == User.Role.ADMIN) {
                logger.info("Admin commands: add, remove, users, user <userId>");
            }
        } else {
            logger.info("Available commands: help, register <name> <password>, login <name> <password>, exit");
        }
    }

    /**
     * Displays all vehicles
     */
    private static void vehicles() {
        logger.info(vehicleRepository.getVehicles().toString());
    }

    /**
     * Rents a vehicle
     * @param id id of the vehicle
     */
    private static void rent(int id) {
        if (vehicleRepository.getIdentifiers().contains(id)) {
            if (vehicleRepository.rentVehicle(currentUser, id)) {
                logger.info("Vehicle rented");
            } else {
                logger.info("Cannot rent vehicle");
            }
        } else {
            logger.warning("Vehicle not found");
        }
    }

    /**
     * Returns a vehicle
     * @param id id of the vehicle
     */
    private static void _return(int id) {
        if (vehicleRepository.getIdentifiers().contains(id)) {
            if (vehicleRepository.returnVehicle(currentUser, id)) {
                logger.info("Vehicle returned");
            } else {
                logger.info("Cannot return vehicle");
            }
        } else {
            logger.warning("Vehicle not found");
        }
    }

    private static void add() {

    }

    private static void remove() {

    }

    /**
     * Displays all users
     */
    private static void users() {
        logger.info(userRepository.getUsers().toString());
    }

    /**
     * Displays a user
     * @param id id of the user
     */
    private static void user(int id) {
        userRepository.getUser(id).ifPresentOrElse(
                user -> logger.info(user.toString()),
                () -> logger.warning("User not found")
        );
    }
}
