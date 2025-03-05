package pl.com.foks.data;

import pl.com.foks.exceptions.RentalException;
import pl.com.foks.vehicle.Vehicle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RentalDataManager {
    private final Path rentals;

    public RentalDataManager(Path rentals) {
        if (!rentals.toFile().exists()) {
            throw new RentalException("Rental data file does not exist");
        }
        this.rentals = rentals;
    }

    public void save(List<Vehicle> vehicles) throws IOException {
        CSVWriter csvWriter = new CSVWriter(rentals);
        csvWriter.write(vehicles);
    }

    public void load(List<Vehicle> vehicles) throws FileNotFoundException {
        CSVReader csvReader = new CSVReader(rentals);
        vehicles.addAll(csvReader.read());
    }
}
