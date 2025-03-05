package pl.com.foks;

import pl.com.foks.data.RentalDataManager;
import pl.com.foks.rental.User;
import pl.com.foks.rental.Rental;
import pl.com.foks.vehicle.Vehicle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static final Path WORKSPACE = Path.of(System.getProperty("user.home"), "rental");
    public static final Path RENTALS = WORKSPACE.resolve("rentals.csv");

    private static Rental rental = null;
    private static Map<User, String> users = new HashMap<>();
    private static User currentUser = null;

    static {
        if (!WORKSPACE.toFile().exists()) {
            WORKSPACE.toFile().mkdirs();
        }
        users.put(new User(User.Role.ADMIN, "admin", User.DriversLicenseCategory.B), "admin");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        InputStream rentals = Main.class.getClassLoader().getResourceAsStream("rentals.csv");
        if (rentals != null) {
            if (RENTALS.toFile().exists()) RENTALS.toFile().delete();
            Files.copy(rentals, RENTALS);
        }

        rental = new Rental(new RentalDataManager(RENTALS));
        rental.load();
        run();
    }

    private static void run() {
        final Scanner scanner = new Scanner(System.in);
        logger.info("""
                Register user (register <name> <driversCategory> <password>)
                Login (login <name> <password>)
                """);
        while (scanner.hasNextLine()) {
            if (currentUser == null) {
                final String line = scanner.nextLine();
                final String[] split = line.split(" ");
                switch (split[0]) {
                    case "help": {
                        logger.info("Available commands: help, register <name> <driversCategory> <password>, login <name> <password>, exit");
                        break;
                    }
                    case "exit": {
                        System.exit(0);
                    }
                    case "login": {
                        final String name = split[1];
                        final String password = split[2];
                        users.entrySet().stream().filter(entry -> Objects.equals(entry.getKey().getName(), name) &&
                                Objects.equals(entry.getValue(), password)).findFirst()
                                .ifPresent(entry -> currentUser = entry.getKey());
                        if (currentUser != null) {
                            logger.info("User logged in");
                        } else {
                            logger.info("Login failed");
                        }
                        break;
                    }
                    case "register": {
                        final String name = split[1];
                        final String driversCategory = split[2];
                        final String password = split[3];
                        if (Arrays.stream(User.DriversLicenseCategory.values()).noneMatch(licenseCategory -> licenseCategory.name().equals(driversCategory))) {
                            logger.info("Register user (register <name> <driversCategory> <password>)");
                            continue;
                        }
                        currentUser = new User(User.Role.CLIENT, name, User.DriversLicenseCategory.valueOf(driversCategory));
                        users.put(currentUser, password);
                        logger.info("User registered");
                        break;
                    }
                    default: {
                        logger.info("""
                            Register user (register <name> <driversCategory> <password>)
                            Login (login <name> <password>)
                            """);
                    }
                }
            } else {
                String line = scanner.nextLine();
                String[] split = line.split(" ");
                switch (split[0]) {
                    case "help": {
                        logger.info("Available commands: help, self, list, rent <vehicleId>, return <vehicleId>, exit, logout");
                        if (currentUser.getRole() == User.Role.ADMIN) {
                            logger.info("Admin commands: add, remove");
                        }
                        break;
                    }
                    case "self": {
                        logger.info(currentUser.toString());
                        break;
                    }
                    case "list": {
                        StringBuilder sb = new StringBuilder("\n");
                        rental.getVehicles().forEach(vehicle -> sb.append(vehicle.getId()).append(". ")
                                .append(vehicle).append("\n"));
                        logger.info(sb.toString());
                        break;
                    }
                    case "rent": {
                        if (split.length == 2) {
                            int id = Integer.parseInt(split[1]);
                            if (id >= 0 && id < rental.getVehicles().size()) {
                                if (rental.rentVehicle(currentUser, rental.getVehicles().get(id))) {
                                    logger.info("Vehicle rented");
                                } else {
                                    logger.info("Cannot rent vehicle");
                                }
                            } else {
                                logger.warning("Vehicle not found");
                            }
                        } else {
                            logger.info("rent <vehicle>");
                        }
                        break;
                    }
                    case "return": {
                        if (split.length == 2) {
                            int id = Integer.parseInt(split[1]);
                            if (id >= 0 && id < rental.getVehicles().size()) {
                                if (rental.returnVehicle(currentUser, rental.getVehicles().get(id))) {
                                    logger.info("Vehicle returned");
                                } else {
                                    logger.info("Cannot return vehicle");
                                }
                            } else {
                                logger.warning("Vehicle not found");
                            }
                        } else {
                            logger.info("return <vehicle>");
                        }
                        break;
                    }
                    case "exit": {
                        rental.save();
                        System.exit(0);
                    }
                    case "logout": {
                        currentUser = null;
                        logger.info("User logged out");
                        logger.info("""
                            Register user (register <name> <driversCategory> <password>)
                            Login (login <name> <password>)
                            """);
                        break;
                    }
                    case "add": {

                    }
                    case "remove": {

                    }
                    default: {
                        logger.info("Available commands: help, self, list, rent <vehicleId>, return <vehicleId>, exit, logout");
                        if (currentUser.getRole() == User.Role.ADMIN) {
                            logger.info("Admin commands: add, remove");
                        }
                    }
                }
            }
        }

    }
}
