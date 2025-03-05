package pl.com.foks;

import pl.com.foks.data.RentalDataManager;
import pl.com.foks.rental.Client;
import pl.com.foks.rental.Rental;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static Rental rental = null;
    private static Client client = null;

    public static final Path WORKSPACE = Path.of(System.getProperty("user.home"), "rental");
    public static final Path RENTALS = WORKSPACE.resolve("rentals.csv");

    static {
        if (!WORKSPACE.toFile().exists()) {
            WORKSPACE.toFile().mkdirs();
        }
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
        logger.info("Create user (user <name> <driversCategory>)");
        while (scanner.hasNextLine()) {
            if (client == null) {
                final String line = scanner.nextLine();
                final String[] split = line.split(" ");
                if (split.length == 3 && split[0].equals("user")) {
                    final String name = split[1];
                    final String driversCategory = split[2];
                    if (Arrays.stream(Client.DriversLicenseCategory.values()).noneMatch(licenseCategory -> licenseCategory.name().equals(driversCategory))) {
                        logger.info("Create user (user <name> <driversCategory>)");
                        continue;
                    }
                    client = new Client(name, Client.DriversLicenseCategory.valueOf(driversCategory));
                    logger.info("User created");
                } else if (split[0].equals("exit")) {
                    System.exit(0);
                } else {
                    logger.info("Create user (user <name> <driversCategory>)");
                }
            } else {
                String line = scanner.nextLine();
                String[] split = line.split(" ");
                switch (split[0]) {
                    case "help": {
                        logger.info("Available commands: help, self, list, rent <vehicleId>, return <vehicleId>, exit");
                        break;
                    }
                    case "self": {
                        logger.info(client.toString());
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
                                if (rental.rentVehicle(client, rental.getVehicles().get(id))) {
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
                                if (rental.returnVehicle(client, rental.getVehicles().get(id))) {
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
                }
            }
        }

    }
}
