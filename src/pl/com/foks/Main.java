package pl.com.foks;

import pl.com.foks.data.UserDataManager;
import pl.com.foks.data.VehicleDataManager;
import pl.com.foks.repository.vehicle.VehicleFactory;
import pl.com.foks.repository.vehicle.VehicleRepository;
import pl.com.foks.repository.user.UserRepository;
import pl.com.foks.repository.vehicle.vehicles.Car;
import pl.com.foks.repository.vehicle.vehicles.Motorcycle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void main(String[] args) throws IOException {
        VehicleFactory.registerVehicleClass(Car.class);
        VehicleFactory.registerVehicleClass(Motorcycle.class);

        init(Boolean.parseBoolean(args[0]));
        new App(vehicleRepository, userRepository).run();
    }

    /**
     * Initializes the program
     * @throws IOException if the file cannot be read
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void init(boolean overrideData) throws IOException {
        if (!WORKSPACE.toFile().exists()) {
            WORKSPACE.toFile().mkdirs();
        }

        InputStream vehicles = Main.class.getClassLoader().getResourceAsStream(VEHICLES_FILE);
        if (vehicles != null) {
            if (!VEHICLES.toFile().exists()) {
                Files.copy(vehicles, VEHICLES);
            } else if (overrideData) {
                VEHICLES.toFile().delete();
                Files.copy(vehicles, VEHICLES);
            }
        }

        InputStream users = Main.class.getClassLoader().getResourceAsStream(USERS_FILE);
        if (users != null) {
            if (!USERS.toFile().exists()) {
                Files.copy(users, USERS);
            } else if (overrideData) {
                USERS.toFile().delete();
                Files.copy(users, USERS);
            }
        }

        vehicleRepository = new VehicleRepository(new VehicleDataManager(VEHICLES));
        vehicleRepository.load();

        userRepository = new UserRepository(new UserDataManager(USERS));
        userRepository.load();
    }
}
