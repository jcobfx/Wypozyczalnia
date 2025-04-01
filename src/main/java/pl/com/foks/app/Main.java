package pl.com.foks.app;

import lombok.Cleanup;
import lombok.Getter;
import pl.com.foks.domain.AuthService;
import pl.com.foks.domain.RentalService;
import pl.com.foks.domain.UserService;
import pl.com.foks.domain.VehicleService;
import pl.com.foks.infrastructure.repository.RentalRepository;
import pl.com.foks.infrastructure.repository.UserRepository;
import pl.com.foks.infrastructure.repository.VehicleRepository;

import java.io.*;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Path WORKSPACE = Path.of(System.getProperty("user.home"), "wypozyczalnia");

    public static void main(String[] args) {
        init();

        RentalRepository rentalRepository = new RentalRepository(Files.RENTALS.getPath().toString());
        VehicleRepository vehicleRepository = new VehicleRepository(Files.VEHICLES.getPath().toString());
        UserRepository userRepository = new UserRepository(Files.USERS.getPath().toString());

        RentalService rentalService = new RentalService(rentalRepository);
        VehicleService vehicleService = new VehicleService(vehicleRepository, rentalService);
        UserService userService = new UserService(userRepository, rentalService, vehicleService);
        AuthService authService = new AuthService(userService);

        new App(rentalService, userService, vehicleService, authService).run();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void init() {
        if (!WORKSPACE.toFile().exists()) {
            WORKSPACE.toFile().mkdirs();
        }
        copyFiles();
    }

    private static void copyFiles() {
        try {
            for (Files file : Files.values()) {
                copyFile(file);
            }
            logger.info("Files created successfully");
        } catch (IOException e) {
            logger.severe("Failed to create files");
        }
    }

    private static void copyFile(Files file) throws IOException {
        if (file.getPath().toFile().createNewFile()) {
            @Cleanup InputStream rentals = Main.class.getClassLoader().getResourceAsStream(file.getFilename());
            assert rentals != null;
            rentals.transferTo(new FileOutputStream(file.getPath().toFile()));
        }
    }

    @Getter
    private enum Files {
        RENTALS("rentals.json"),
        USERS("users.json"),
        VEHICLES("vehicles.json");

        private final String filename;

        Files(String filename) {
            this.filename = filename;
        }

        public Path getPath() {
            return WORKSPACE.resolve(filename);
        }
    }
}
